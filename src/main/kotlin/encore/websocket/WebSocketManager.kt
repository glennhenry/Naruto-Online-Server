package encore.websocket

import encore.fancam.Fancam
import encore.fancam.INDENT
import encore.fancam.Tags
import encore.utils.support.className
import encore.websocket.handler.WebSocketHandler
import io.ktor.server.websocket.*
import kotlinx.serialization.json.JsonElement
import java.util.concurrent.ConcurrentHashMap

/**
 * WebSocket client representation.
 *
 * @property id Unique identifier WS client ID.
 * @property session Reference to the websocket connection.
 * @property listens The set of WS messages the client cares about.
 *                   The server should only send a WS message if it exists here.
 */
data class WebSocketClient(
    val id: String,
    val session: DefaultWebSocketServerSession,
    val listens: Set<String>
)

/**
 * Track websocket connections and dispatch incoming message to handlers.
 * This websocket system is mostly used to connect with the external devtools.
 *
 * Usage:
 * - Register handler via [registerHandler].
 * - Add client with [addClient].
 * - Dispatch message to handlers via [handleMessage].
 */
class WebSocketManager {
    private val clients = ConcurrentHashMap<String, WebSocketClient>()

    // the number of clients that listen to newlog message
    private var listeningToNewLog = 0

    /**
     * Add a new websocket session.
     *
     * Does nothing if the client was added before.
     *
     * @param clientId Unique identifier to associate with the session.
     * @param listens Set of WS message type the clients wants to receive.
     *                Comma-separated string.
     * @param session The websocket connection.
     */
    fun addClient(clientId: String, listens: String, session: DefaultWebSocketServerSession) {
        if (!clients.contains(clientId)) {
            clients[clientId] = WebSocketClient(
                id = clientId,
                listens = listens.split(",").map { it.trim() }.toSet(),
                session = session
            ).also {
                if (it.listens.contains(WebSocketMessageType.NewLog) || it.listens.contains("all")) {
                    listeningToNewLog += 1
                }
            }
        }
    }

    /**
     * Remove the tracked websocket session associated with [clientId].
     */
    fun removeClient(clientId: String) {
        clients.remove(clientId)?.let {
            if (it.listens.contains(WebSocketMessageType.NewLog) || it.listens.contains("all")) {
                listeningToNewLog -= 1
            }
        }
    }

    /**
     * Returns the `clientId` of all actively tracked websocket session.
     */
    fun getAllClients(): List<String> {
        return clients.keys.toList()
    }

    /**
     * Returns `true` if there is at least one client listening
     * to the [type] of WS message.
     */
    fun clientExists(type: String): Boolean {
        if (type == WebSocketMessageType.NewLog) {
            return listeningToNewLog > 0
        }
        return clients.values.find { it.listens.contains(type) } != null
    }

    /**
     * Get the associated websocket session of [clientId].
     *
     * @return WS session or `null` if the client is not found.
     */
    fun getSessionFromId(clientId: String): DefaultWebSocketServerSession? {
        return clients[clientId]?.session
    }

    private val handlers = mutableMapOf<String, WebSocketHandler>()

    /**
     * Register [wsHandler] to handle message of type [WebSocketHandler.type].
     *
     * The handling system doesn't support multiple handler per message type.
     * Duplicate registration of the same type will be skipped.
     */
    fun registerHandler(wsHandler: WebSocketHandler) {
        if (handlers[wsHandler.type] != null) {
            Fancam.warn(Tags.Websocket) { "WebSocketHandler for '${wsHandler.type}' is already registered, skipping." }
        } else {
            handlers[wsHandler.type] = wsHandler
        }
    }

    /**
     * Handle the websocket [message] received from the client [session].
     *
     * If no handler is found, the message will simply be logged and left unhandled.
     */
    suspend fun handleMessage(session: DefaultWebSocketServerSession, message: WebSocketMessage) {
        val handler = handlers[message.type]

        Fancam.debug(Tags.Websocket) {
            buildString {
                appendLine("##### [WebSocket Receive]")
                appendLine("$INDENT type       : ${message.type}")
                appendLine("$INDENT payload    : ${message.payload}")
                append("$INDENT handled by : ${handler?.className() ?: "[Unhandled]"}")
            }
        }

        if (handler == null) {
            Fancam.warn(Tags.Websocket) { "No handler is registered for WebSocket message '${message.type}'" }
        }

        handler?.handle(message, session)
    }

    /**
     * Sends a WS message to all client.
     *
     * This will also filter the target client depending on their
     * [WebSocketClient.listens].
     *
     * @param type The type of the WS message.
     * @param payload The payload.
     */
    suspend fun sendToAllClients(type: String, payload: JsonElement) {
        clients.values.forEach { client ->
            if (client.listens.contains(type) || client.listens.contains("all")) {
                client.session.sendJson(client.id, type, payload)
            }
        }
    }
}
