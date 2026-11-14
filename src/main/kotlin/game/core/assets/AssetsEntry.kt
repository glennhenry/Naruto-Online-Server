package game.core.assets

/**
 * Represent Naruto Online assets entry that is found on resource.cfg.
 *
 * @property url Relative path to the assets.
 *               e.g., `assets/dir1/dir2/file.swf`
 * @property tag Version tag of the assets.
 *               e.g., `EN_NarutoAlpha6.00Build304`
 * @property size Size of the assets in bytes.
 * @property ver Revision number of this assets.
 * @property belong Unknown. It's always null in the CFG.
 */
data class AssetsEntry(
    val url: AssetsPath,
    val tag: VersionTag,
    val size: Int,
    val ver: Int,
    // this is always null
    val belong: String? = null,
)

/**
 * Returns `true` if this is an optional assets.
 *
 * See [AssetsPath.isOptionalAssets]
 */
fun AssetsEntry.isOptionalAssets(): Boolean {
    return url.isOptionalAssets()
}
