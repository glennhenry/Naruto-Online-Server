package game.protocol

import encore.fancam.Fancam
import encore.fancam.Tags
import encore.network.handler.AllRounderHandler
import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext

/**
 * Fallback handler for all [NarutoFanchant] that is unrecognized in
 * [NarutoFanchantGuide].
 *
 * If a particular command happens to be added in [NarutoFanchantGuide]'s materialize,
 * it won't use the fixed `naruto-fallback` type and therefore gets routed to [AllRounderHandler]
 * instead.
 */
class NarutoFallbackHandler : FanchantHandler<FallbackNarutoFanchant> {
    override val fanchantType: String = "naruto-fallback"

    override suspend fun handle(ctx: HandlerContext<FallbackNarutoFanchant>) = with(ctx) {
        Fancam.warn(Tags.Socket) { "Unhandled fanchant: $fanchant" }
    }
}
