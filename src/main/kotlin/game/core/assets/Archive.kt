package game.core.assets

/**
 * Represent the 'archive' entry inside each of either
 * assets division: `flash`, `assets`, and `config` of [ArchivePack].
 *
 * @property num The number of items of this archive, same as items' [List.size].
 * @property id The unique identifier of this archive within multiple
 *              archives inside one of the assets division.
 * @property path The path of this archive, typically it's a `.pkg` file.
 * @property t Unknown, a timestamp of this archive.
 * @property items The list of items this archive consists of.
 */
data class Archive(
    val num: Int,
    val id: Int,
    // not every archive has url
    val path: AssetsPath?,
    // some archive have a 't' which is some unknown timestamp
    val t: String?,
    val items: List<AssetsPath>
)

/**
 * Returns the total amount of items from each archive.
 *
 * This isn't list size!
 */
fun List<Archive>.size(): Int {
    return sumOf { it.num }
}
