package game.assets.mirror

import encore.fancam.Fancam
import game.assets.common.BaseAssetsDir
import game.utils.formatSizeInMb
import game.utils.millisFormatInSeconds
import java.awt.Desktop
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * Component to reproduce Naruto Online remote assets.
 *
 * Reproducing operation includes:
 * - [downloadRequiredFiles]: Downloads all required files
 * - [downloadOptionalFiles]: Downloads optional files
 * - [deleteRemoveableFiles]: Removes unneeded files (older version)
 * - [deleteOptionallyRemoveableFiles]: Removes optional files (version is newer, but unneeded)
 *
 * The [optimalMirror] provides an optimal reproduce behavior by:
 * - Downloading all required files, ignoring the optional
 * - Removing any files that is not listed in the remote index
 *
 * The [mirror] provides a true reproduce behavior by:
 * - Downloading all listed files in the remote index, including the optional
 * - Removing any files that is not listed in the remote index
 *
 * @property sync The result of [AssetsSyncer.sync]
 * @property assetsDir The base assets directory.
 */
class AssetsReproducer(
    private val sync: AssetsSyncResult,
    private val assetsDir: String = BaseAssetsDir
) {
    /**
     * Download all required files in [AssetsSyncResult.download]
     * using [AssetsDownloader].
     *
     * @param maxConcurrency Maximum amount of downloads at a time.
     */
    suspend fun downloadRequiredFiles(maxConcurrency: Int = 5) {
        if (sync.download.isEmpty()) {
            Fancam.info("download") { "No required files to download" }
            return
        }
        Fancam.info("download") {
            "${sync.download.size} " +
                    "files to download...> ${sync.downloadSize.formatSizeInMb()}"
        }
        val elapsed = measureTimeMillis {
            val downloader = AssetsDownloader(sync.download.toList(), assetsDir, maxConcurrency)
            downloader.start()
                .filter { it.isFailure }
                .forEach {
                    Fancam.warn { "Previous error download: ${it.exceptionOrNull()?.message}" }
                }
        }
        Fancam.info("download") {
            ">>>>>>>>>> Finished required downloads in ${elapsed.millisFormatInSeconds()} <<<<<<<<<<"
        }
    }

    /**
     * Download all optional files in [AssetsSyncResult.optionalDownload]
     * using [AssetsDownloader].
     *
     * @param maxConcurrency Maximum amount of downloads at a time.
     */
    suspend fun downloadOptionalFiles(maxConcurrency: Int = 5) {
        if (sync.optionalDownload.isEmpty()) {
            Fancam.info("download") { "No optional files to download" }
            return
        }
        Fancam.info("download") {
            "${sync.optionalDownload.size} " +
                    "optional files to download...> ${sync.optionalDownloadSize.formatSizeInMb()}"
        }
        val elapsed = measureTimeMillis {
            val downloader = AssetsDownloader(sync.optionalDownload.toList(), assetsDir, maxConcurrency)
            downloader.start()
                .filter { it.isFailure }
                .forEach {
                    Fancam.warn { "Previous error download: ${it.exceptionOrNull()?.message}" }
                }
        }
        Fancam.info("download") {
            ">>>>>>>>>> Finished required downloads in ${elapsed.millisFormatInSeconds()} <<<<<<<<<<"
        }
    }

    /**
     * Moves all removeable files in [AssetsSyncResult.remove] to the system's trash.
     * It will delete the file directly if system trash is not supported.
     */
    fun deleteRemoveableFiles() {
        if (sync.remove.isEmpty()) {
            Fancam.info("remover") { "No removeable files to delete" }
            return
        }

        Fancam.info("remover") { ">>> List of files that will be removed:" }
        Fancam.info("remover") { "(files will be moved to trash/recycle bin)" }

        val elapsed = measureTimeMillis {
            for (path in sync.remove) {
                val file = File(path)
                if (file.exists()) {
                    println(file.path)
                    moveToTrash(file)
                }
            }
        }

        Fancam.info("remover") { ">>>>>>>>>> All files moved to trash in ${elapsed.millisFormatInSeconds()} <<<<<<<<<<" }
    }

    /**
     * Moves all optionally removeable files in [AssetsSyncResult.optionalRemove] to the system's trash.
     * It will delete the file directly if system trash is not supported.
     */
    fun deleteOptionallyRemoveableFiles() {
        if (sync.optionalRemove.isEmpty()) {
            Fancam.info("remover") { "No optionally removeable files to delete" }
            return
        }

        Fancam.info("remover") { ">>> List of optional files that will be removed:" }
        Fancam.info("remover") { "(files will be moved to trash/recycle bin)" }

        val elapsed = measureTimeMillis {
            for (path in sync.optionalRemove) {
                val file = File(path)
                if (file.exists()) {
                    println(file.path)
                    moveToTrash(file)
                }
            }
        }

        Fancam.info("remover") { ">>>>>>>>>> All files moved to trash in ${elapsed.millisFormatInSeconds()} <<<<<<<<<<" }
    }

    /**
     * Perform a true reproduce operation by:
     * - Downloading every assets the remote index listed, including the optional.
     * - Removing any files that is not listed on the remote index
     *
     * @param maxConcurrency Maximum amount of downloads at a time.
     */
    suspend fun mirror(maxConcurrency: Int = 5) {
        Fancam.info("mirror") { "Starting mirror..." }
        val elapsed = measureTimeMillis {
            downloadRequiredFiles(maxConcurrency)
            downloadOptionalFiles(maxConcurrency)
            deleteRemoveableFiles()
            deleteOptionallyRemoveableFiles()
        }
        Fancam.info("mirror") { "Mirror finished in ${elapsed.millisFormatInSeconds()}" }
    }

    /**
     * Perform an optimal reproduce operation by:
     * - Downloading all required files, ignoring the optional
     * - Removing any files that is not listed in the remote index
     *
     * @param maxConcurrency Maximum amount of downloads at a time.
     */
    suspend fun optimalMirror(maxConcurrency: Int = 5) {
        Fancam.info("mirror") { "Starting optimal mirror..." }
        val elapsed = measureTimeMillis {
            downloadRequiredFiles(maxConcurrency)
            deleteRemoveableFiles()
            deleteOptionallyRemoveableFiles()
        }
        Fancam.info("mirror") { "Optimal mirror finished in ${elapsed.millisFormatInSeconds()}" }
    }

    private val desktop: Desktop? = if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop()
    } else {
        null
    }

    private fun hasTrash(): Boolean {
        return desktop?.isSupported(Desktop.Action.MOVE_TO_TRASH) ?: false
    }

    private fun moveToTrash(file: File) {
        if (hasTrash() && desktop != null) {
            if (!desktop.moveToTrash(file)) {
                file.delete()
            }
        } else {
            file.delete()
        }
    }
}
