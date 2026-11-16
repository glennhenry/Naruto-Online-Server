package game.assets.mirror

import encore.fancam.Fancam
import game.utils.formatSizeInKb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.net.URI
import java.util.concurrent.atomic.AtomicInteger

/**
 * Component to download assets.
 *
 * Use [start].
 *
 * @property urls List of [DownloadUrl] to download.
 *                Uses [DownloadUrl.directory] for directory and
 *                [DownloadUrl.filename] for filename.
 * @property outputDir The base output directory.
 * @property maxConcurrency Max concurrent downloads.
 */
class AssetsDownloader(
    private val urls: List<DownloadUrl>,
    private val outputDir: String,
    private val maxConcurrency: Int = 5
) {
    /**
     * Start downloading assets.
     *
     * - Assets are downloaded in concurrent manner, with the limit of [maxConcurrency].
     * - Assets are saved to the base directory [outputDir], then the parent's
     *   directory of each [DownloadUrl.directory], and as the filename [DownloadUrl.filename].
     * - It will always download and overwrite the existing file on disk.
     *
     * @return A list which is the results of all file downloads.
     */
    suspend fun start(): List<Result<File>> {
        return coroutineScope {
            val progress = AtomicInteger(0)
            val total = urls.size
            File(outputDir).mkdirs()
            val dispatcher = Dispatchers.IO.limitedParallelism(maxConcurrency)

            val jobs = urls.map { url ->
                async(dispatcher) {
                    try {
                        val directory = File(outputDir, url.directory).also { it.mkdirs() }
                        val targetFile = File(directory, url.filename)
                        URI(url.toCdnUrl()).toURL().openStream().use { input ->
                            targetFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        Fancam.info {
                            "Done (${progress.incrementAndGet()}/$total): " +
                                    "${url.directory}/${url.filename} " +
                                    targetFile.length().formatSizeInKb()
                        }
                        Result.success(targetFile)
                    } catch (e: Exception) {
                        Fancam.error(e) { "Scandal while downloading" }
                        Result.failure(e)
                    }
                }
            }
            jobs.awaitAll()
        }
    }
}
