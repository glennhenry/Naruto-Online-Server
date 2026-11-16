import game.assets.versioning.ResourceCfg
import java.io.File

/**
 * The following file contains pieces of Naruto Online tools.
 *
 * Access to the tools are written in tests as main function instead of
 * independent dev endpoint for simplicity.
 *
 * It should only contain interfaces, not actual tools.
 * The actual tools are written in the main packages.
 *
 * How to create the interface?
 * - Create `object` with a `main` function and annotate with `@JvmStatic`
 * - The `main` function contains the code you want to run.
 *   Use it, for example, to run certain tools with the input args.
 *
 * How to run?
 * - Click the run button next to the main function.
 * - Use from terminal like `.\gradlew runTool -Ptool=<toolname>`
 * - Optionally, supply args with `--args=...`
 */

/**
 * Decode resource.cfg, then export it to JSON and XML.
 *
 * Run: `.\gradlew runTool -Ptool=ExportResourceCfg`
 */
object ExportResourceCfg {
    @JvmStatic
    fun main(args: Array<String>) {
        val res = File("workspace/resource.cfg")
        val rescfg = ResourceCfg(res).also { it.decode() }
        rescfg.versionMap.exportToJson("workspace/output")
        rescfg.archives.exportToXml("workspace/output")
    }
}
