package game.character.register

import encore.network.handler.FanchantHandler
import encore.network.handler.HandlerContext
import game.protocol.definition.NarutoFanchant
import game.protocol.definition.ProtocolCmd
import game.protocol.definition.cmdtype
import game.protocol.response.response

class RecommendRoleNameHandler : FanchantHandler<NarutoFanchant<RecommendRoleNameRequest>> {
    override val fanchantType: String = cmdtype(ProtocolCmd.CMD_RECOMMEND_NAME)

    override suspend fun handle(ctx: HandlerContext<NarutoFanchant<RecommendRoleNameRequest>>) = with(ctx) {
        // although client expected a list of string,
        // it only take the first element
        // so randomize here, but supply single array
        val names = if (fanchant.payload.gender == 1) {
            listOf(maleNames.random())
        } else {
            listOf(femaleNames.random())
        }
        connection.write(response(RecommendRoleNameResponse(names), fanchant))
    }

    val maleNames = listOf(
        "UzumakiNaruto",
        "HatakeKakashi",
        "SasukeUchiha",
        "ItachiUchiha",
        "HashiramaSenju"
    )

    val femaleNames = listOf(
        "SakuraHaruno",
        "Tenten",
        "HyugaHinata",
        "TsunadeSenju",
        "KurenaiYuhi"
    )
}
