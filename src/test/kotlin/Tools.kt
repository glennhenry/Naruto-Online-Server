import game.assets.common.BaseAssetsDir
import game.assets.common.VersionTag
import game.assets.format.CfgDataFormat
import game.assets.format.XmlDataFormat
import game.assets.mirror.AssetsReproducer
import game.assets.mirror.AssetsSyncer
import game.assets.mirror.LocalAssetsIndex
import game.assets.versioning.ResourceCfg
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * The following file contains pieces of devtools and utility code.
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
 * Find files as you want by editing the code directly.
 *
 * This tool is a ready-to-use utility you can use to find files
 * in the assets directory. It can run faster than Windows file explorer.
 *
 * Run: `.\gradlew runTool -Ptool=FindFiles`
 */
object FindFiles {
    @JvmStatic
    fun main(args: Array<String>) {
        val dir = File(BaseAssetsDir)
        val files = dir.walk()
            .filter { it.isFile }
            .toList()
            .map { it.path to it.extension }
        println(files.joinToString("\n"))
    }
}

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

/**
 * Encode back a decoded `versionMap.json` and `archives.xml`
 * to `resource.cfg`.
 *
 * Both `archives.xml` and `versionMap.json` is produced from
 * tools like [ExportResourceCfg].
 *
 * Note: both must be present in the workspace directory.
 *
 * Find your output in `workspace/output`.
 *
 * Run: `.\gradlew runTool -Ptool=EncodeResourceCfg`
 */
object EncodeResourceCfg {
    @JvmStatic
    fun main(args: Array<String>) {
        val versionMap = File("workspace/versionMap.json")
        val archives = File("workspace/archives.xml")
        ResourceCfg.encodeToFile(versionMap, archives, "workspace/output")
    }
}

/**
 * Update Naruto Online assets by downloading new files
 * and removing old unneeded files.
 *
 * Run: `.\gradlew runTool -Ptool=UpdateAssets`
 */
object UpdateAssets {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val res = File("workspace/resource.cfg")
        val rescfg = ResourceCfg(res).also { it.decode() }
        val index = LocalAssetsIndex(BaseAssetsDir).also { it.build() }
        val sync = AssetsSyncer(index, rescfg.versionMap).sync()
        AssetsReproducer(sync, BaseAssetsDir)
            .optimalMirror()
    }
}

/**
 * Find a specific assets file across all versions.
 *
 * Arguments:
 * - filename (e.g., `ArenaBadgeCFG.xml`) case doesn't matter
 *
 * Either input the argument as gradle args or change the filename variable.
 * We prioritizes the filename string.
 *
 * The tool will search the file across the assets directory
 * and return every available versions. The search performed is
 * `contains`, meaning the search result will find any filename
 * that contains the given input.
 *
 * Run: `.\gradlew runTool -Ptool=FindAssetFile --args=<filename>`
 */
object FindAssetFile {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        // change the string directly or input args
        val filename = "CHANGE_ME"
        val final = if (filename == "CHANGE_ME") {
            args.firstOrNull() ?: error("Please input filename argument")
        } else {
            filename
        }

        println("Searching for $final...")

        val dir = File(BaseAssetsDir)
        val files = dir.walk()
            .filter { it.isFile && it.name.contains(final, ignoreCase = true) }
            .toList()
            .map { f ->
                val relativePath = f.relativeTo(dir).invariantSeparatorsPath
                val (version, _) = relativePath.split('/', limit = 2)
                f to VersionTag(version)
            }

        if (files.isEmpty()) {
            println("No file found with $final")
        }

        files.forEachIndexed { idx, (f, ver) ->
            println("$idx) Found ${f.name} of version ${ver.tag}: ${f.path}")
        }
    }
}

/**
 * Decode and export Naruto Online `.xml` data.
 *
 * Arguments:
 * - filename (e.g., `ArenaBadgeCFG.xml`) case doesn't matter
 *
 * Either input the argument as gradle args or change the filename variable.
 * We prioritizes the filename string.
 *
 * The tool will search the file across the assets directory,
 * find the latest version, and use it.
 *
 * Output is at `workspace/output`, same file name.
 *
 * Run: `.\gradlew runTool -Ptool=ExportXmlData --args=<filename>`
 */
object ExportXmlData {
    @JvmStatic
    fun main(args: Array<String>) {
        // change the string directly or input args
        val filename = "CHANGE_ME"
        val final = if (filename == "CHANGE_ME") {
            args.firstOrNull() ?: error("Please input filename argument")
        } else {
            filename
        }

        println("Searching for $final...")

        val dir = File(BaseAssetsDir)
        val files = dir.walk()
            .filter { it.isFile && it.name.equals(final, ignoreCase = true) }
            .toList()
            .map { f ->
                val relativePath = f.relativeTo(dir).invariantSeparatorsPath
                val (version, _) = relativePath.split('/', limit = 2)
                f to VersionTag(version)
            }

        if (files.isEmpty()) {
            println("No file found with $final")
            return
        }

        val (file, ver) = files.reduce { prev, cur ->
            if (prev.second.isNewerThan(cur.second)) prev else cur
        }

        println("Found ${file.name} of version ${ver.tag}: ${file.path}")
        XmlDataFormat(file).also { it.decode() }
            .exportToFile("workspace/output")

        println("Exported successfully to workspace/output")
    }
}

/**
 * Decode and export Naruto Online `.xml` data.
 *
 * Arguments:
 * - filename (e.g., `ArenaBadgeCFG.xml`) case doesn't matter
 *
 * Similar to [ExportXmlData], but this search the workspace
 * instead of the assets directory.
 *
 * Output is at `workspace/output`, same file name.
 *
 * Run: `.\gradlew runTool -Ptool=ExportXmlDataFromWorkspace --args=<filename>`
 */
object ExportXmlDataFromWorkspace {
    @JvmStatic
    fun main(args: Array<String>) {
        // change the string directly or input args
        val filename = "CHANGE_ME"
        val final = if (filename == "CHANGE_ME") {
            args.firstOrNull() ?: error("Please input filename argument")
        } else {
            filename
        }

        println("Searching for $final in the workspace...")

        val dir = File("workspace")
        val file = dir.walk()
            .filter { it.isFile && it.name.equals(final, ignoreCase = true) }
            .toList()
            .firstOrNull() ?: error("Couldn't find $final in the workspace.")

        println("Found ${file.name}: ${file.path}")
        XmlDataFormat(file).also { it.decode() }
            .exportToFile("workspace/output")

        println("Exported successfully to workspace/output")
    }
}

/**
 * Encode a plain XML data file back into CFG.
 *
 * When an XML data file is decoded (e.g., using tools like [ExportXmlData])
 * and edited, you can use this to encode it back to original format.
 *
 * Arguments:
 * - filename (e.g., `ArenaBadgeCFG.xml`).
 *   This must be present in the workspace directory.
 *
 * Either input the argument as gradle args or change the filename variable.
 * We prioritizes the filename string.
 *
 * Output is at `workspace/output`, same file name.
 *
 * Run: `.\gradlew runTool -Ptool=EncodeXmlData --args=<filename>`
 */
object EncodeXmlData {
    @JvmStatic
    fun main(args: Array<String>) {
        // change the string directly or input args
        val filename = "ArenaBadgeCFG.xml"
        val final = if (filename == "CHANGE_ME") {
            args.firstOrNull() ?: error("Please input filename argument")
        } else {
            filename
        }

        val file = File("workspace", final)
        if (!file.exists() || !file.isFile) {
            error("Ensure that the file is present in 'workspace' directory")
        }

        XmlDataFormat.encodeToFile(file, "workspace/output")
        println("Encoded successfully to workspace/output")
    }
}

/**
 * Decode and export Naruto Online `.cfg` data.
 *
 * Arguments:
 * - filename (e.g., `SpringBathConfigCFG.cfg`) case doesn't matter
 *
 * Either input the argument as gradle args or change the filename variable.
 * We prioritizes the filename string.
 *
 * The tool will search the file across the assets directory,
 * find the latest version, and use it.
 *
 * Output is at `workspace/output`, same file name.
 *
 * Run: `.\gradlew runTool -Ptool=ExportCfgData --args=<filename>`
 */
object ExportCfgData {
    @JvmStatic
    fun main(args: Array<String>) {
        // change the string directly or input args
        val filename = "CHANGE_ME"
        val final = if (filename == "CHANGE_ME") {
            args.firstOrNull() ?: error("Please input filename argument")
        } else {
            filename
        }

        println("Searching for $final...")

        val dir = File(BaseAssetsDir)
        val files = dir.walk()
            .filter { it.isFile && it.name.equals(final, ignoreCase = true) }
            .toList()
            .map { f ->
                val relativePath = f.relativeTo(dir).invariantSeparatorsPath
                val (version, _) = relativePath.split('/', limit = 2)
                f to VersionTag(version)
            }

        if (files.isEmpty()) {
            println("No file found with $final")
            return
        }

        val (file, ver) = files.reduce { prev, cur ->
            if (prev.second.isNewerThan(cur.second)) prev else cur
        }

        println("Found ${file.name} of version ${ver.tag}: ${file.path}")
        CfgDataFormat(file).also { it.decode() }
            .exportToFile("workspace/output")

        println("Exported successfully to workspace/output")
    }
}

/**
 * Decode and export Naruto Online `.cfg` data.
 *
 * Arguments:
 * - filename (e.g., `SpringBathConfigCFG.cfg`) case doesn't matter
 *
 * Similar to [ExportCfgData], but this search the workspace
 * instead of the assets directory.
 *
 * Output is at `workspace/output`, same file name.
 *
 * Run: `.\gradlew runTool -Ptool=ExportCfgDataFromWorkspace --args=<filename>`
 */
object ExportCfgDataFromWorkspace {
    @JvmStatic
    fun main(args: Array<String>) {
        // change the string directly or input args
        val filename = "CardItemRecycleCFG.cfg"
        val final = if (filename == "CHANGE_ME") {
            args.firstOrNull() ?: error("Please input filename argument")
        } else {
            filename
        }

        println("Searching for $final in the workspace...")

        val dir = File("workspace")
        val file = dir.walk()
            .filter { it.isFile && it.name.equals(final, ignoreCase = true) }
            .toList()
            .firstOrNull() ?: error("Couldn't find $final in the workspace.")

        println("Found ${file.name}: ${file.path}")
        CfgDataFormat(file).also { it.decode() }
            .exportToFile("workspace/output")

        println("Exported successfully to workspace/output")
    }
}

/**
 * Encode a plain JSON data file back into CFG.
 *
 * When a CFG data file is decoded (e.g., using tools like [ExportCfgData])
 * produced into JSON and edited, you can use this to encode it back to
 * original format.
 *
 * Arguments:
 * - filename (e.g., `CardItemRecycleCFG.json`).
 *   This must be present in the workspace directory.
 *
 * Either input the argument as gradle args or change the filename variable.
 * We prioritizes the filename string.
 *
 * Output is at `workspace/output`, same file name.
 *
 * Run: `.\gradlew runTool -Ptool=EncodeCfgData --args=<filename>`
 */
object EncodeCfgData {
    @JvmStatic
    fun main(args: Array<String>) {
        // change the string directly or input args
        val filename = "CardItemRecycleCFG.json"
        val final = if (filename == "CHANGE_ME") {
            args.firstOrNull() ?: error("Please input filename argument")
        } else {
            filename
        }

        val file = File("workspace", final)
        if (!file.exists() || !file.isFile) {
            error("Ensure that the file is present in 'workspace' directory")
        }

        CfgDataFormat.encodeToFile(file, "workspace/output")
        println("Encoded successfully to workspace/output")
    }
}