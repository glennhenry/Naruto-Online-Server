package game.items

import kotlinx.serialization.Serializable

@Serializable
data class ItemInfo(
    val id: UInt, // probably unique identifier
    val num: Int, // item amount
    // probably item ID -> telling which item is this
    // must lookup the assets to know the IDs
    val index: UInt
) {
    companion object {
        fun dummy(): ItemInfo {
            return ItemInfo(
                id = (1u..1000u).random(),
                num = 12,
                // seal scroll
                index = 10800047u
            )
        }
    }
}
