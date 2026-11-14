package gameTest.core.assets

import game.assets.common.VersionTag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Some consideration:
 * - Minor number is always 2 digit at maximum.
 * - Build number is always '300' or more.
 */
class VersionTagTest {
    @Test
    fun `init success`() {
        val tag = VersionTag("EN_NarutoAlpha6.12Build304")
        assertEquals(6, tag.major)
        assertEquals(12, tag.minor)
        assertEquals(304, tag.build)
    }

    @Test
    fun `init success zero-leading minor number`() {
        val tag = VersionTag("EN_NarutoAlpha6.02Build304")
        assertEquals(6, tag.major)
        assertEquals(2, tag.minor)
        assertEquals(304, tag.build)

        val tag2 = VersionTag("EN_NarutoAlpha6.00Build304")
        assertEquals(6, tag2.major)
        assertEquals(0, tag2.minor)
        assertEquals(304, tag2.build)
    }

    @Test
    fun `isNewerThan compare with greater version returns true`() {
        assertTrue {
            VersionTag("EN_NarutoAlpha7.00Build300")
                .isNewerThan(VersionTag("EN_NarutoAlpha6.00Build300"))
        }
        assertTrue {
            VersionTag("EN_NarutoAlpha6.01Build300")
                .isNewerThan(VersionTag("EN_NarutoAlpha6.00Build300"))
        }
        assertTrue {
            VersionTag("EN_NarutoAlpha6.00Build301")
                .isNewerThan(VersionTag("EN_NarutoAlpha6.00Build300"))
        }
    }

    @Test
    fun `isNewerThan compare with same version returns false`() {
        assertFalse {
            VersionTag("EN_NarutoAlpha6.00Build300")
                .isNewerThan(VersionTag("EN_NarutoAlpha6.00Build300"))
        }
    }

    @Test
    fun `isOlderThan compare with greater version returns false`() {
        assertFalse {
            VersionTag("EN_NarutoAlpha7.00Build300")
                .isOlderThan(VersionTag("EN_NarutoAlpha6.00Build300"))
        }
        assertFalse {
            VersionTag("EN_NarutoAlpha6.01Build300")
                .isOlderThan(VersionTag("EN_NarutoAlpha6.00Build300"))
        }
        assertFalse {
            VersionTag("EN_NarutoAlpha6.00Build301")
                .isOlderThan(VersionTag("EN_NarutoAlpha6.00Build300"))
        }
    }

    @Test
    fun `isOlderThan compare with same version returns false`() {
        assertFalse {
            VersionTag("EN_NarutoAlpha6.00Build300")
                .isOlderThan(VersionTag("EN_NarutoAlpha6.00Build300"))
        }
    }

    @Test
    fun `isSameVersion returns true for same version`() {
        assertTrue {
            VersionTag("EN_NarutoAlpha6.00Build300")
                .isSameVersion(VersionTag("EN_NarutoAlpha6.00Build300"))
        }
    }
}
