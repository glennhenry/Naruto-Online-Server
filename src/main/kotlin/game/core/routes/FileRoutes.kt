package game.core.routes

import encore.route.RouteHandler
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

/**
 * Serve file-related endpoints.
 *
 * This mostly serving static files:
 * - Game and website assets in the `assets` folder.
 * - Docs website on production in the `docs_build` folder.
 *
 * Since this is simple, it doesn't use the [RouteHandler]
 */
fun Route.fileRoutes() {
    get("/") {
        call.respondFile(File("assets/site/index.html"))
    }
    staticFiles("site", File("assets/site"))

    val docsDir = File("docs_build")
    if (File(docsDir, "index.html").exists()) {
        staticFiles("docs", docsDir)
    } else {
        get("/docs") {
            call.respond(
                HttpStatusCode.NotFound,
                "Docs website not available. Please start it with a separate vite server. " +
                        "If in prod, build the documentation website to access it."
            )
        }
    }
}
