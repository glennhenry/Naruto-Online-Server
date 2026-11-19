package game.assets.mirror

import encore.fancam.Fancam
import encore.fancam.INDENT
import encore.utils.formatSizeInMb
import game.assets.common.AssetsPath
import game.assets.common.BaseAssetsDir
import game.assets.versioning.RemoteAssetsEntry
import game.assets.versioning.ResourceCfg
import game.assets.versioning.VersionMap

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
     * - List of URLs to download
     * - List of URLs to download optionally.
     * - List of local assets path to remove (prepended wwith [assetsDir]).
     * - List of local assets path to remove optionally.
     * - The bytes size of each results.
     *
     * @param assetsDir Local assets directory.
     */
    fun sync(assetsDir: String = BaseAssetsDir): AssetsSyncResult {
        val download = mutableSetOf<DownloadUrl>()
        var downloadSize = 0L
        val optionalDownload = mutableSetOf<DownloadUrl>()
        var optionalDownloadSize = 0L
        val remove = mutableSetOf<String>()
        var removeSize = 0L
        val optionalRemove = mutableSetOf<String>()
        var optionalRemoveSize = 0L

        fun addDownload(remoteEntry: RemoteAssetsEntry) {
            if (remoteEntry.url.isOptionalAssets()) {
                optionalDownload.add(
                    DownloadUrl(
                        "${remoteEntry.tag.tag}/${remoteEntry.url.directoryPath}",
                        remoteEntry.url.filename
                    )
                )
                optionalDownloadSize += remoteEntry.size
            } else {
                download.add(
                    DownloadUrl(
                        "${remoteEntry.tag.tag}/${remoteEntry.url.directoryPath}",
                        remoteEntry.url.filename
                    )
                )
                downloadSize += remoteEntry.size
            }
        }

        for (remoteEntry in remote.entries()) {
            val onDisk = local.getEntry(remoteEntry.url.path)

            // local has no existing assets that matches the remote listed version
            // -> download the listed version
            if (onDisk.none { it.version.isSameVersion(remoteEntry.tag) }) {
                addDownload(remoteEntry)
            }

            // local has some existing assets
            onDisk.forEach { localEntry ->
                if (localEntry.version.isOlderThan(remoteEntry.tag)) {
                    // local has older assets, while remote listed newer -> download newer, remove older
                    remove.add(localEntry.path.toLocalPath(localEntry.version.tag, assetsDir))
                    removeSize += localEntry.size
                } else if (localEntry.version.isNewerThan(remoteEntry.tag)) {
                    // local has newer assets, while remote listed older -> download older, optionally remove newer
                    optionalRemove.add(localEntry.path.toLocalPath(localEntry.version.tag, assetsDir))
                    optionalRemoveSize += localEntry.size
                }
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
