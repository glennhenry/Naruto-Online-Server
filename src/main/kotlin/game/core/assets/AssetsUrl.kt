package game.core.assets

import java.io.File

/**
 * Base CDN URL of Naruto Online assets.
 * This is fixed for EN version.
 */
const val BaseCdnUrl = "cdnnarutoen-cst.oasgames.com"

/**
 * The base path for local assets directory.
 * This path is before each assets' version tag.
 */
const val BaseAssetsDir = "assets/game"

/**
 * Relative path to the assets.
 * It does not include the local or remote CDN URL and version tag.
 * For example: `assets/dir1/dir2/file.swf`.
 *
 * @property url The full relative path of the assets.
 */
data class AssetsUrl(val url: String) {
    /**
     * The path segment of the assets.
     *
     * e.g., `assets/dir1/dir2`
     */
    val path: String

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
        url.split(Regex("/(?=[^/]*$)"), limit = 2).let {
            path = it[0]
            filename = it[1]
        }

        filename.split('.', limit = 2).let {
            filenameWithoutExt = it[0]
            extension = it[1]
        }
    }

    /**
     * Convert into CDN URL with [BaseCdnUrl] and given [versionTag].
     *
     * e.g., `cdnnarutoen-cst.oasgames.com/EN_NarutoAlpha6.00Build304/assets/dir1/dir2/file.swf`
     */
    fun toCdnUrl(versionTag: String): String {
        return "$BaseCdnUrl/$versionTag/$url"
    }

    /**
     * Convert into local path with [BaseAssetsDir] and given [versionTag].
     *
     * e.g., `assets/game/EN_NarutoAlpha6.00Build304/assets/dir1/dir2/file.swf`
     */
    fun toLocalPath(versionTag: String): String {
        return "$BaseAssetsDir/$versionTag/$url"
    }

    /**
     * Return file representation of this assets.
     * This does not validate the availablility of the file.
     */
    fun asFile(versionTag: String): File {
        return File(toLocalPath(versionTag))
    }
}
