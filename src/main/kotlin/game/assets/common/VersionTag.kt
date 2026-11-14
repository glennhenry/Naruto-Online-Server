package game.assets.common

/**
 * Regex for Naruto Online version tag, separating between the
 * major, minor, and build number.
 */
val VersionTagRegex = """EN_NarutoAlpha(\d+).(\d+)Build(\d+)""".toRegex()

/**
 * Represent the version information of a particular
 * Naruto Online assets retrieved from the resource.cfg.
 *
 * @property tag The original tag retrieved from the CFG.
 *               e.g., `EN_NarutoAlpha6.00Build304`.
 */
data class VersionTag(val tag: String) {
    /**
     * Returns the major version number.
     *
     * e.g., `6` in `EN_NarutoAlpha6.00Build304`
     */
    val major: Int

    /**
     * Returns the minor version number.
     *
     * e.g., `0` in `EN_NarutoAlpha6.00Build304`
     */
    val minor: Int

    /**
     * Returns the build version number.
     *
     * e.g., `304` in `EN_NarutoAlpha6.00Build304`
     */
    val build: Int

    init {
        val match = VersionTagRegex.find(tag)
        require(match != null && match.groupValues.size > 3) {
            "Invalid regex on tag '$tag'. Possible regex mistake?"
        }
        major = match.groupValues[1].toInt()
        minor = match.groupValues[2].toInt()
        build = match.groupValues[3].toInt()
    }

    /**
     * Returns `true` if this assets version is same with [other].
     */
    fun isSameVersion(other: VersionTag): Boolean {
        return major == other.major && minor == other.minor && build == other.build
    }

    /**
     * Returns `true` if this assets version is newer than [other].
     *
     * Comparison is done from the [major], [minor], then the [build] number.
     */
    fun isNewerThan(other: VersionTag): Boolean {
        if (major > other.major) {
            return true
        }
        if (minor > other.minor) {
            return true
        }
        if (build > other.build) {
            return true
        }
        return false
    }

    /**
     * Returns `true` if this assets version is older than [other].
     *
     * Comparison is done from the [major], [minor], then the [build] number.
     */
    fun isOlderThan(other: VersionTag): Boolean {
        if (other.major > major) {
            return true
        }
        if (other.minor > minor) {
            return true
        }
        if (other.build > build) {
            return true
        }
        return false
    }
}
