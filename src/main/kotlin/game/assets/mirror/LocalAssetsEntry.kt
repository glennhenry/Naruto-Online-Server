package game.assets.mirror

import game.assets.common.AssetsPath
import game.assets.common.BaseAssetsDir
import game.assets.common.BaseCdnUrl
import game.assets.common.VersionTag
import java.io.File

/**
 * Represent assets that is found on the local user's directory.
 * @property path Path of the assets.
 * @property version The version of the assets.
 * @property size The size of the assets in bytes.
 */
data class LocalAssetsEntry(
    val path: AssetsPath,
    val version: VersionTag,
    val size: Int
) {
    /**
     * Convert into CDN URL with [cdnUrl].
     *
     * e.g., `cdnnarutoen-cst.oasgames.com/EN_NarutoAlpha6.00Build304/assets/dir1/dir2/file.swf`
     *
     * Utilizes [AssetsPath.toCdnUrl].
     */
    fun toCdnUrl(cdnUrl: String = BaseCdnUrl): String {
        return this.path.toCdnUrl(version.tag, cdnUrl)
    }

    /**
     * Convert into local path with [assetsDir].
     *
     * e.g., `assets/game/EN_NarutoAlpha6.00Build304/assets/dir1/dir2/file.swf`
     *
     * Utilizes [AssetsPath.toCdnUrl].
     */
    fun toLocalPath(assetsDir: String = BaseAssetsDir): String {
        return this.path.toLocalPath(version.tag, assetsDir)
    }

    /**
     * Return file representation of this assets.
     * This does not validate the availablility of the file.
     *
     * Utilizes [AssetsPath.toCdnUrl].
     */
    fun asFile(): File {
        return File(toLocalPath())
    }
}
