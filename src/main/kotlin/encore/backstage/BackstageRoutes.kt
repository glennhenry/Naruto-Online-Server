package encore.backstage

import encore.EncoreIdentity
import encore.backstage.command.MonitorData
import encore.backstage.command.core.getMemoryStats
import encore.backstage.command.core.getRuntimeInfo
import encore.backstage.command.core.getUptimeInfo
import encore.backstage.command.types.CommandRequest
import encore.fancam.Fancam
import encore.fancam.Tags
import encore.route.RouteHandler
import encore.route.guard
import encore.route.guard.AuthGuard
import encore.route.guard.GuardResult
import encore.route.guard.NoAuthGuard
import encore.route.handle
import encore.serialization.JSON
import encore.time.TimeCenter
import encore.utils.identifier.Ids
import encore.venue.Venue
import encore.websocket.WebSocketMessage
import encore.websocket.WebSocketMessageType
import game.GameIdentity
import game.context.ServerContext
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.utils.io.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/**
 * Backstage routes (devtools). In the backstage, there are three sections of tools:
 *  - **Logger**: live log stream from server.
 *  - **Monitor**: server status and activity from server (e.g., active players, server state, running task).
 *  - **Command**: external input to control server runtime.
 *
 * Authentication/usage flow:
 * 1. User go to `/backstage`.
 * 2. Server respond with `wall.html`, asking for token.
 * 3. User type `token` in terminal to obtain an ephemeral token (1 minute).
 * 4. Server validates the token from user (via query parameters).
 *     - If invalid, send back `wall.html` (back to step 2).
 *     - If valid, set cookie of `backstage-clientId` with a new generated token (valid for 6 hours).
 *     Then, server responds with `backstage.html`.
 * 5. Client-side connects to websocket, including the previous token as a query parameter.
 * 6. Server verify the WS query parameter.
 *     - If invalid, refuse the connection. This will prevent arbitrary websocket connection.
 *     The page will still be valid, but client can't do anything.
 *     - If valid, then websocket connection is approved.
 *     - From now on, refreshing page does not prompt `wall.html` anymore.
 * 7. Client and server starts exchanging WS messages for tools.
 *     - Server streams log message to client for logger using websocket.
 *     - Client periodically make API request to `/monitor`, including the cookie token
 *     to get monitoring status.
 *         - If cookie token is invalid, server return error status.
 *         - If cookie token is valid, server return JSON for server status.
 *     - Client can type command and it will be executed in the server.
 * 8. When session exceeded 6 hours, user needs to refresh. (step 6A).
 */
class BackstageRoutes(
    private val serverContext: ServerContext,
    private val tokenStorage: MutableMap<String, Long>
) : RouteHandler {
    private val backstageAuthGuard = BackstageAuthGuard { verifyToken(it) }
    private var cmdData = ""
    private var metadata = ""

    fun verifyToken(token: String): Boolean {
        return serverContext.subunits.session.verify(token)
    }

    override fun Route.install() {
        staticFiles("backstage/libs", File("backstage/libs"))
        staticFiles("backstage/assets", File("backstage/assets"))

        get("/backstage") {
            handle(call, NoAuthGuard) {
                val wallHtml = File("backstage/wall.html")
                val indexHtml = File("backstage/index.html")

                // skip auth on developmentMode
                if (Venue.encore.devMode) {
                    Fancam.trace(Tags.Backstage) { "Auth skipped on /backstage (development mode)" }
                    call.respondFile(indexHtml)
                    return@handle
                }

                val token = call.request.queryParameters["token"]
                val cookie = call.request.cookies["backstage-clientId"]

                // PASS: user with cookie has already authenticated before
                if (cookie != null && verifyToken(cookie)) {
                    Fancam.trace(Tags.Backstage) { "Passed to /backstage: cookie available" }
                    call.respondFile(indexHtml)
                    return@handle
                }

                // WALL: user with cookie but expired
                if (cookie != null && !verifyToken(cookie)) {
                    Fancam.trace(Tags.Backstage) { "Wall to /backstage: cookie has expired" }
                    call.respondText(
                        insertHtmlTemplate(wallHtml, "{{MESSAGE}}", "Cookie expired"),
                        ContentType.Text.Html
                    )
                    return@handle
                }

                // WALL: user without cookie and without token.
                if (token == null) {
                    Fancam.trace(Tags.Backstage) { "Wall to /backstage: no token provided" }
                    call.respondText(insertHtmlTemplate(wallHtml, "{{MESSAGE}}", "Insert token"), ContentType.Text.Html)
                    return@handle
                }

                // WALL: user has unknown token
                if (!tokenStorage.contains(token)) {
                    Fancam.trace(Tags.Backstage) { "Wall to /backstage: got unknown token: $token" }
                    call.respondText(
                        insertHtmlTemplate(wallHtml, "{{MESSAGE}}", "Unknown token"),
                        ContentType.Text.Html
                    )
                    return@handle
                }

                // WALL: user has known token, but expired
                if (tokenStorage.contains(token) && TimeCenter.hasElapsedBy(tokenStorage[token]!!, 1.minutes)) {
                    Fancam.trace(Tags.Backstage) { "Wall to /backstage: token already expired" }
                    call.respondText(
                        insertHtmlTemplate(wallHtml, "{{MESSAGE}}", "Token expired"),
                        ContentType.Text.Html
                    )
                    return@handle
                }

                // PASS: user has valid token
                val session = serverContext.subunits.session.create(
                    userId = Ids.uuid(), validFor = 6.hours, lifetime = 6.hours
                )
                call.response.cookies.append("backstage-clientId", session.token, maxAge = 21600, path = "/backstage")
                Fancam.trace(Tags.Backstage) { "Passed to /backstage: token is valid" }
                call.respondFile(indexHtml)
            }
        }

        get("/backstage/status") {
            guard(call, backstageAuthGuard) {
                call.respond(HttpStatusCode.OK)
            }
        }

        get("/backstage/metadata") {
            guard(call, backstageAuthGuard) {
                if (metadata.isBlank()) {
                    metadata = JSON.encode(
                        mapOf(
                            "encoreVersion" to EncoreIdentity.Version,
                            "encoreVersionDate" to EncoreIdentity.VersionDate,
                            "encoreCodename" to EncoreIdentity.Codename,
                            "serverName" to GameIdentity.Title,
                            "serverVersion" to GameIdentity.Version,
                        )
                    )
                }
                call.respond(metadata)
            }
        }

        get("/backstage/init") {
            guard(call, backstageAuthGuard) {
                val runtimeInfo = getRuntimeInfo()
                val uptimeInfo = getUptimeInfo()
                call.respond(
                    JSON.encode(
                        mapOf(
                            "javaVersion" to runtimeInfo.javaVersion,
                            "osDetails" to runtimeInfo.osDetails,
                            "serverTime" to uptimeInfo.serverTime.toString(),
                            "runningSince" to uptimeInfo.runningSince.toString()
                        )
                    )
                )
            }
        }

        get("/backstage/monitor") {
            guard(call, backstageAuthGuard) {
                call.respond(
                    JSON.encode(
                        MonitorData(
                            numPlayers = serverContext.subunits.presence.getNumOnlinePlayers(),
                            memStats = getMemoryStats(),
                            acts = serverContext.stageActDirector.getAllRunningActs()
                        )
                    )
                )
            }
        }

        get("/backstage/logger") {
            handle(call, backstageAuthGuard) {
                call.respondFile(File("backstage/logger.html"))
            }
        }

        get("/backstage/command") {
            handle(call, backstageAuthGuard) {
                call.respondFile(File("backstage/command.html"))
            }
        }

        get("/backstage/command-data") {
            guard(call, backstageAuthGuard) {
                if (cmdData.isBlank()) {
                    val commands = serverContext.commandDispatcher.getAllRegisteredCommands()
                    cmdData = JSON.encode<List<CommandData>>(commands.map { cmd ->
                        val args = cmd.arguments.map { arg ->
                            ArgumentData(arg.id, arg.name, arg.description, arg.type.name, arg.defaultValue.toString())
                        }
                        CommandData(cmd.id, cmd.name, cmd.description, args)
                    })
                }
                call.respond(cmdData)
            }
        }

        post("/backstage/command") {
            handle(call, backstageAuthGuard) {
                val request = JSON.decode<CommandRequest>(call.receiveText())
                val result = serverContext.commandDispatcher.handleCommand(request, serverContext)
                call.respond(
                    JSON.encode(
                        mapOf(
                            "result" to result.toString(),
                            "message" to result.message
                        )
                    )
                )
            }
        }

        webSocket("/backstage/logger") {
            val token = if (Venue.encore.devMode) {
                // dev mode uses arbitrary identifier
                "DEV-${TimeCenter.now()}"
            } else {
                // websocket can't send cookie, token cookie for WS is included in the param instead
                // then verify the token
                call.request.queryParameters["token"]
                    ?.takeIf { verifyToken(it) }
            }

            if (token == null) {
                Fancam.trace(Tags.Backstage) { "Can't connect to /backstage/logger WebSocket with invalid token=$token" }
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Invalid token"))
                return@webSocket
            }

            val listens = call.request.queryParameters["listens"] ?: run {
                Fancam.trace(Tags.Backstage) { "Can't connect to /backstage/logger WebSocket with bad request: missing 'listens'." }
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Bad request missing listens"))
                return@webSocket
            }
            serverContext.webSocketManager.addClient(
                clientId = token,
                listens = listens,
                session = this
            )

            Fancam.trace(Tags.Backstage) { "Client with token '$token' is now connected to /backstage/logger WebSocket" }

            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val msg = frame.readText()
                        try {
                            val wsMessage = Json.decodeFromString<WebSocketMessage>(msg)
                            if (wsMessage.type == WebSocketMessageType.Close) {
                                serverContext.webSocketManager.removeClient(token)
                                break
                            }
                            serverContext.webSocketManager.handleMessage(this, wsMessage)
                        } catch (e: Exception) {
                            Fancam.error(e, Tags.Backstage) { "Failed to parse WS message: $msg" }
                        }
                    }
                }
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                Fancam.error(e, Tags.Backstage) { "Scandal on /backstage/logger WebSocket for clientId=$token" }
            } finally {
                serverContext.webSocketManager.removeClient(token)
                Fancam.trace(Tags.Backstage) { "Client $token is disconnected from websocket" }
            }
        }
    }
}

/**
 * Simple helper for HTML templating.
 *
 * This will replace every occurence of `templateId` with [message] in the [file].
 */
fun insertHtmlTemplate(file: File, templateId: String, message: String): String {
    return file.readText().replace(templateId, message)
}

/**
 * Auth guard that ensure a backstage session is valid.
 *
 * Valid requests must send a non-expired cookie of `backstage-clientId`.
 *
 * @param verifyFunction Function used to verify the validity of the cookie.
 */
class BackstageAuthGuard(private val verifyFunction: (String) -> Boolean) : AuthGuard {
    override suspend fun verify(call: ApplicationCall): GuardResult {
        val cookie = call.request.cookies["backstage-clientId"]
        val cookieValid = cookie != null && verifyFunction(cookie)

        if (!Venue.encore.devMode && !cookieValid) {
            call.respond(HttpStatusCode.Forbidden, "Session invalid. Please re-login.")
            return GuardResult.Reject("Session invalid")
        }

        return GuardResult.Welcome
    }
}
