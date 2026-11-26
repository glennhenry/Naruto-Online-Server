package game.protocol

import encore.fancam.Fancam
import encore.fancam.Tags
import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext

/**
 * Fallback handler for all unregistered/unrecognized [NarutoFanchant].
 */
class NarutoFallbackHandler : FanchantHandler<FallbackNarutoFanchant> {
    override val fanchantType: String = "-1"

    override suspend fun handle(ctx: HandlerContext<FallbackNarutoFanchant>) = with(ctx) {
        Fancam.warn(Tags.Socket) { "Unhandled fanchant: $fanchant" }
    }
}
