package game.assets.mirror

/**
 * The result of [AssetsSyncer.sync].
 *
 * @property download Set of CDN URLs (without `http://`) required to download.
 * @property downloadSize Size in bytes of the required download.
 * @property optionalDownload Set of CDN URLs optionally to download.
 * @property optionalDownloadSize Size in bytes of the optional download.
 * @property remove Set of local path that can be removed.
 * @property removeSize Size in bytes of the removeable files.
 * @property optionalRemove Set of local path that can be removed optionally.
 * @property optionalRemoveSize Size in bytes of the optionally removeable files.
 */
data class AssetsSyncResult(
    val download: Set<String>,
    val downloadSize: Long,
    val optionalDownload: Set<String>,
    val optionalDownloadSize: Long,
    val remove: Set<String>,
    val removeSize: Long,
    val optionalRemove: Set<String>,
    val optionalRemoveSize: Long
)
