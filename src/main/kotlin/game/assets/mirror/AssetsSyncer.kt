package game.assets.mirror

import encore.fancam.Fancam
import encore.fancam.INDENT
import game.assets.common.AssetsPath
import game.assets.common.BaseAssetsDir
import game.assets.common.BaseCdnUrl
import game.assets.versioning.RemoteAssetsEntry
import game.assets.versioning.ResourceCfg
import game.assets.versioning.VersionMap
import game.utils.formatSizeInMb

/**
 * Component to sync between the assets on local disk with the
 * remote listed assets on the version map.
 *
 * Usage: [sync].
 *
 * @property local [LocalAssetsIndex].
 * @property remote [VersionMap] of [ResourceCfg].
 */
class AssetsSyncer(
    private val local: LocalAssetsIndex,
    private val remote: VersionMap
) {
    /**
     * Sync between local and remote assets, producing an [AssetsSyncResult].
     *
     * This operation is performed to mirror the local assets entries
     * with the remote assets entries.
     *
     * - Every assets listed in the remote index must be downloaded,
     *   unless the assets is optional ([AssetsPath.isOptionalAssets])
     *   or it's already on local.
     * - When a newer version of assets is added to download,
     *   the older version can be removed.
     * - If the local has a newer version assets than the one listed
     *   on the remote index, the remote listed assets still needs
     *   to be downloaded, but the local version may be removed optionally.
     *
     * The result contains:
     * - List of CDN URLs to download (prepended with [cdnUrl] without HTTP declaration).
     * - List of CDN URLs for optional downloads.
     * - List of local assets path to remove (prepended wwith [assetsDir]).
     * - List of local assets path to remove optionally.
     * - The bytes size of each results.
     *
     * @param assetsDir Local assets directory.
     * @param cdnUrl The CDN URL.
     */
    fun sync(assetsDir: String = BaseAssetsDir, cdnUrl: String = BaseCdnUrl): AssetsSyncResult {
        val download = mutableSetOf<String>()
        var downloadSize = 0L
        val optionalDownload = mutableSetOf<String>()
        var optionalDownloadSize = 0L
        val remove = mutableSetOf<String>()
        var removeSize = 0L
        val optionalRemove = mutableSetOf<String>()
        var optionalRemoveSize = 0L

        fun addDownload(remoteEntry: RemoteAssetsEntry) {
            if (remoteEntry.url.isOptionalAssets()) {
                optionalDownload.add(remoteEntry.url.toCdnUrl(remoteEntry.tag.tag, cdnUrl))
                optionalDownloadSize += remoteEntry.size
            } else {
                download.add(remoteEntry.url.toCdnUrl(remoteEntry.tag.tag, cdnUrl))
                downloadSize += remoteEntry.size
            }
        }

        for (remoteEntry in remote.entries()) {
            val onDisk = local.getEntry(remoteEntry.url.path)
            if (onDisk.isNotEmpty()) {
                onDisk.forEach { localEntry ->
                    if (localEntry.version.isOlderThan(remoteEntry.tag)) {
                        // local has older, remote has newer -> download newer, remove older
                        addDownload(remoteEntry)
                        remove.add(localEntry.path.toLocalPath(localEntry.version.tag, assetsDir))
                        removeSize += localEntry.size
                    } else if (localEntry.version.isNewerThan(remoteEntry.tag)) {
                        // local has newer, remote has older -> download older, optionally remove newer
                        addDownload(remoteEntry)
                        optionalRemove.add(localEntry.path.toLocalPath(localEntry.version.tag, assetsDir))
                        optionalRemoveSize += localEntry.size
                    } else {
                        // local has same version as remote listed -> ignore
                    }
                }
            } else {
                // assets in remote but not in local -> download
                addDownload(remoteEntry)
            }
        }

        Fancam.info {
            buildString {
                appendLine("AssetsSyncResult:")
                appendLine(
                    "$INDENT ${download.size} files required to download ${downloadSize.formatSizeInMb()}" +
                            " / ${
                                local.totalSizeBytes().formatSizeInMb()
                            } -> ${(local.totalSizeBytes() + downloadSize).formatSizeInMb()}"
                )
                appendLine(
                    "$INDENT ${optionalDownload.size} files optionally to download ${optionalDownloadSize.formatSizeInMb()}" +
                            " / ${
                                local.totalSizeBytes().formatSizeInMb()
                            } -> ${(local.totalSizeBytes() + optionalDownloadSize).formatSizeInMb()}"
                )
                appendLine(
                    "$INDENT ${remove.size} files can be removed ${removeSize.formatSizeInMb()}" +
                            " / ${
                                local.totalSizeBytes().formatSizeInMb()
                            } -> ${(local.totalSizeBytes() - removeSize).formatSizeInMb()}"
                )
                appendLine(
                    "$INDENT ${optionalRemove.size} files can be removed optionally ${optionalRemoveSize.formatSizeInMb()}" +
                            " / ${
                                local.totalSizeBytes().formatSizeInMb()
                            } -> ${(local.totalSizeBytes() - optionalRemoveSize).formatSizeInMb()}"
                )
                append(
                    "$INDENT Ideal disk size after required download and remove, ignoring optional: ${
                        local.totalSizeBytes().formatSizeInMb()
                    } + ${downloadSize.formatSizeInMb()} - ${removeSize.formatSizeInMb()} - " +
                            "${optionalRemoveSize.formatSizeInMb()} = " +
                            (local.totalSizeBytes() + downloadSize - removeSize - optionalRemoveSize).formatSizeInMb()
                )
            }
        }

        return AssetsSyncResult(
            download,
            downloadSize,
            optionalDownload,
            optionalDownloadSize,
            remove,
            removeSize,
            optionalRemove,
            optionalRemoveSize
        )
    }
}
