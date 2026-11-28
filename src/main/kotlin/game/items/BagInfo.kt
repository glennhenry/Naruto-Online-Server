package game.items

import kotlinx.serialization.Serializable

// I think this depends on the item
// if its an equipment -> fill equip and left everything else empty
// but why is info is a list?
@Serializable
data class BagInfo(
    val itemCellNum: UInt,
    val item: List<BagItemInfo>,
    val taskItem: List<TaskItemInfo>,
    val equip: List<EquipInfo>,
    val card: List<BagCardInfo>,
    val summomMonsterCard: List<BagSummonMonsterCardInfo>,
    val rune: List<BagRuneInfo>,
    val treasureMap: List<BagTreasureMapInfo>,
    val stone: List<BagStoneInfo>,
    val familyNinjaGiftItem: List<BagFamilyNinjaGiftItemInfo>,
    val baojuList: List<BagBaojuInfo>,
    val skillScrollList: List<BagSkillScrollInfo>,
    val timerItem: List<CommonItemInfo>,
    val skillBookList: List<CommonItemInfo>,
) {
    companion object {
        fun dummy(): BagInfo {
            return BagInfo(
                itemCellNum = 1u,
                item = listOf(BagItemInfo.dummy()),
                taskItem = emptyList(),
                equip = emptyList(),
                card = emptyList(),
                summomMonsterCard = emptyList(),
                rune = emptyList(),
                treasureMap = emptyList(),
                stone = emptyList(),
                familyNinjaGiftItem = emptyList(),
                baojuList = emptyList(),
                skillScrollList = emptyList(),
                timerItem = emptyList(),
                skillBookList = emptyList()
            )
        }
    }
}
