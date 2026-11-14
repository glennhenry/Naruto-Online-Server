package encore.route.guard

import io.ktor.server.application.*

/**
 * [AuthGuard] implementation that always allows the request.
 */
object NoAuthGuard : AuthGuard {
    override suspend fun verify(call: ApplicationCall): GuardResult {
        return GuardResult.Welcome
    }
}
