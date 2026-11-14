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
 */
data class LocalAssetsEntry(
    val path: AssetsPath,
    val version: VersionTag
) {
    /**
     * Convert into CDN URL with [cdnUrl] and the given [versionTag].
     *
     * e.g., `cdnnarutoen-cst.oasgames.com/EN_NarutoAlpha6.00Build304/assets/dir1/dir2/file.swf`
     *
     * Utilizes [AssetsPath.toCdnUrl].
     */
    fun toCdnUrl(versionTag: String, cdnUrl: String = BaseCdnUrl): String {
        return this.path.toCdnUrl(versionTag, cdnUrl)
    }

    /**
     * Convert into local path with [assetsDir] and the given [versionTag].
     *
     * e.g., `assets/game/EN_NarutoAlpha6.00Build304/assets/dir1/dir2/file.swf`
     *
     * Utilizes [AssetsPath.toCdnUrl].
     */
    fun toLocalPath(versionTag: String, assetsDir: String = BaseAssetsDir): String {
        return this.path.toLocalPath(versionTag, assetsDir)
    }

    /**
     * Return file representation of this assets.
     * This does not validate the availablility of the file.
     *
     * Utilizes [AssetsPath.toCdnUrl].
     */
    fun asFile(versionTag: String): File {
        return File(toLocalPath(versionTag))
    }
}
