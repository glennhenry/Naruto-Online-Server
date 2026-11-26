package game.core.routes

import encore.route.RouteHandler
import encore.venue.Venue
import game.assets.common.BaseAssetsDir
import io.ktor.http.HttpStatusCode
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

class GameRoutes : RouteHandler {
    override fun Route.install() {
        staticFiles("game", File("assets/game"))
        get("/resource.cfg") {
            call.respondFile(File("workspace/resource.cfg"))
        }
        get("/entry.swf") {
            call.respondFile(File("${BaseAssetsDir}/${Venue.custom.version}/entry.swf"))
        }
        get("/logincgi") {
            call.respond(HttpStatusCode.OK)
        }
        get("/{rest...}") {
            val rest = call.parameters.getAll("rest").orEmpty()
            val version = rest.first()
            val path = rest.drop(1).joinToString("/")
            call.respondFile(File("$BaseAssetsDir/$version/$path"))
        }
    }
}
