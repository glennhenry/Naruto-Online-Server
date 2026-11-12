import game.core.assets.ResourceCfg
import testUtils.TestFancam
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Playground for quick testing and code run
 *
 * .\gradlew test --tests "Playground.play" --console=plain
 */
class Playground {
    @BeforeTest
    fun setup() {
        TestFancam.create()
    }

    @Test
    fun play() {
        val res = File("workspace/resource.cfg")
        val rescfg = ResourceCfg(res).also { it.decode() }
    }
}
