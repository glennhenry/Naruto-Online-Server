package game.assets.format

/**
 * Represent one of the Naruto Online assets data format.
 *
 * Generally, Naruto Online data and configuration files are
 * specially encoded. This format represent a decoded in-memory
 * representation of the file.
 *
 * It's supposed to generically represent certain file
 * and provide generic APIs to it. Advanced use case for the data
 * may implement dedicated class to provide strong typing and
 * a more specific APIs.
 *
 * - [decode]: Decode the file. Each format may have different encoding scheme.
 * - [exportToFile]: Export the file in the decoded representation.
 */
interface DataFormat {
    /**
     * Decode the file.
     *
     * Should do the necessary post operation such as
     * populating local variables for APIs.
     */
    fun decode()

    /**
     * Export the file.
     *
     * @param directoryPath The directory path where the file will be saved to.
     *                      The original filename will be re-used.
     */
    fun exportToFile(directoryPath: String)
}
