package gameTest.core.assets

import game.core.assets.AssetsUrl
import game.core.assets.BaseAssetsDir
import game.core.assets.BaseCdnUrl
import kotlin.test.Test
import kotlin.test.assertEquals

class AssetsUrlTest {
    @Test
    fun `init success`() {
        val assets = AssetsUrl("assets/arena/badge/1.png")
        assertEquals("assets/arena/badge", assets.path)
        assertEquals("1.png", assets.filename)
        assertEquals("1", assets.filenameWithoutExt)
        assertEquals("png", assets.extension)
    }

    @Test
    fun `toCdnUrl correct`() {
        val assets = AssetsUrl("assets/arena/badge/1.png")
        assertEquals(
            "$BaseCdnUrl/EN_NarutoAlpha6.00Build304/assets/arena/badge/1.png",
            assets.toCdnUrl("EN_NarutoAlpha6.00Build304")
        )
    }

    @Test
    fun `toLocalPath correct`() {
        val assets = AssetsUrl("assets/arena/badge/1.png")
        assertEquals(
            "$BaseAssetsDir/EN_NarutoAlpha6.00Build304/assets/arena/badge/1.png",
            assets.toLocalPath("EN_NarutoAlpha6.00Build304")
        )
    }
}
