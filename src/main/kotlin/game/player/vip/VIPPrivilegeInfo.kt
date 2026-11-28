package game.player.vip

import game.items.ItemInfo
import game.player.character.PlayerVipLevel
import game.player.common.NaurtoAllPropertyInfo
import kotlinx.serialization.Serializable

@Serializable
data class VIPPrivilegeInfo(
    val vipLevel: PlayerVipLevel,
    val price: Int,
    val discountPrice: Int,
    val lastDay: Int,
    val autoOfferTask: Boolean,
    val autoHuntLife: Boolean,
    val autoSignIn: Boolean,
    val strength: Int,
    val strengthBuyNum: Int,
    val dungeonExpPrecent: Int,
    val eliteDungeonNum: Int,
    val bringinMoney: Int,
    val expeditionMoneyPrecent: Int,
    val rankBattleNum: Int,
    val patchSignIn: Int,
    val dailyMoneyCoupon: Int,
    val firstReward: List<ItemInfo>,
    val weekendReward: Boolean,
    val property: NaurtoAllPropertyInfo,
    val canGetFirstReward: Boolean,
    val sceneTeleportTimes: Int,
    val discountPrice2: Int,
) {
    companion object {
        fun dummy(vipLevel: PlayerVipLevel): VIPPrivilegeInfo {
            return VIPPrivilegeInfo(
                vipLevel = vipLevel,
                price = 101,
                discountPrice = 100,
                lastDay = 0,
                autoOfferTask = true,
                autoHuntLife = true,
                autoSignIn = true,
                strength = 1,
                strengthBuyNum = 1,
                dungeonExpPrecent = 15,
                eliteDungeonNum = 5,
                bringinMoney = 2,
                expeditionMoneyPrecent = 4,
                rankBattleNum = 10,
                patchSignIn = 1,
                dailyMoneyCoupon = 200,
                firstReward = listOf(ItemInfo.dummy()),
                weekendReward = true,
                property = NaurtoAllPropertyInfo.dummy(),
                canGetFirstReward = true,
                sceneTeleportTimes = 20,
                discountPrice2 = 100
            )
        }
    }
}
