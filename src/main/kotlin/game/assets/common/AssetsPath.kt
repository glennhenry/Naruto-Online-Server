package game.assets.common

import java.io.File

/**
 * Base CDN URL of Naruto Online assets.
 * This is fixed for the EN version.
 */
const val BaseCdnUrl = "cdnnarutoen-cst.oasgames.com"

/**
 * The base path for local assets directory.
 * This path is before each assets' version tag.
 */
const val BaseAssetsDir = "assets/game"

/**
 * Representation of assets' path.
 *
 * It's independent of local assets directory, remote CDN URL, and
 * version tag.
 *
 * For example: `assets/dir1/dir2/file.swf`.
 *
 * @property path The path of the assets.
 */
data class AssetsPath(val path: String) {
    /**
     * The directory path of this assets.
     *
     * e.g., `assets/dir1/dir2`
     *
     * This can be empty for file that live in the root directory
     * such as `entry.swf`.
     */
    val directoryPath: String

    /**
     * Filename of the assets.
     *
     * e.g., `file.swf`
     */
    val filename: String

    /**
     * Filename without extension.
     *
     * e.g., `file`
     */
    val filenameWithoutExt: String

    /**
     * File extension of the assets.
     *
     * e.g., `swf`
     */
    val extension: String

    init {
        val pathParts = path.split(Regex("/(?=[^/]*$)"), limit = 2)
        if (pathParts.size == 2) {
            directoryPath = pathParts[0]
            filename = pathParts[1]
        } else {
            // edge case: root-level file like entry.swf
            directoryPath = ""
            filename = pathParts[0]
        }

        val filenameParts = filename.split('.', limit = 2)
        if (filenameParts.size == 2) {
            filenameWithoutExt = filenameParts[0]
            extension = filenameParts[1]
        } else {
            // edge case: some assets don't have extension and its size is 0
            // such as treasureHunting tipsPic
            filenameWithoutExt = filenameParts[0]
            extension = ""
        }
    }

    /**
     * Convert into CDN URL with [cdnUrl] and the given [versionTag].
     *
     * e.g., `cdnnarutoen-cst.oasgames.com/EN_NarutoAlpha6.00Build304/assets/dir1/dir2/file.swf`
     */
    fun toCdnUrl(versionTag: String, cdnUrl: String = BaseCdnUrl): String {
        return "$cdnUrl/$versionTag/$path"
    }

    /**
     * Convert into local path with [assetsDir] and the given [versionTag].
     *
     * e.g., `assets/game/EN_NarutoAlpha6.00Build304/assets/dir1/dir2/file.swf`
     */
    fun toLocalPath(versionTag: String, assetsDir: String = BaseAssetsDir): String {
        return "$assetsDir/$versionTag/$path"
    }

    /**
     * Return file representation of this assets.
     * This does not validate the availablility of the file.
     */
    fun asFile(versionTag: String): File {
        return File(toLocalPath(versionTag))
    }

    /**
     * Returns `true` if this assets is optional.
     *
     * This refers to assets that has no significance for the game
     * and is not required to be downloaded. It includes `Thumbs.db`
     * and some assets that are never used by the game.
     */
    fun isOptionalAssets(): Boolean {
        // @TODO add assets blacklist for unimportant assets like rumorPlot plotImg
        return path.contains("Thumbs.db")
    }
}
