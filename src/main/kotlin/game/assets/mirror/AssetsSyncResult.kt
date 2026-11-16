package game.assets.mirror

import game.assets.common.BaseCdnUrl
import game.utils.ensureHttp

/**
 * The result of [AssetsSyncer.sync].
 *
 * @property download Set of CDN URLs required to download.
 * @property downloadSize Size in bytes of the required download.
 * @property optionalDownload Set of CDN URLs optionally to download.
 * @property optionalDownloadSize Size in bytes of the optional download.
 * @property remove Set of local path that can be removed.
 * @property removeSize Size in bytes of the removeable files.
 * @property optionalRemove Set of local path that can be removed optionally.
 * @property optionalRemoveSize Size in bytes of the optionally removeable files.
 */
data class AssetsSyncResult(
    val download: Set<DownloadUrl>,
    val downloadSize: Long,
    val optionalDownload: Set<DownloadUrl>,
    val optionalDownloadSize: Long,
    val remove: Set<String>,
    val removeSize: Long,
    val optionalRemove: Set<String>,
    val optionalRemoveSize: Long
)

/**
 * Represent the URL to download.
 *
 * @property directory The directory path, including the version tag.
 *                     e.g., `EN_NarutoAlpha6.00Build304/assets/dir1/dir2`
 * @property filename The filename.
 *                     e.g., `file.swf`
 */
data class DownloadUrl(
    val directory: String,
    val filename: String
) {
    /**
     * Convert into CDN URL with [cdnUrl] with HTTP declaration included.
     *
     * e.g., `https://cdnnarutoen-cst.oasgames.com/EN_NarutoAlpha6.00Build304/assets/dir1/dir2/file.swf`
     */
    fun toCdnUrl(cdnUrl: String = BaseCdnUrl): String {
        return "$cdnUrl/$directory/$filename".ensureHttp()
    }
}
