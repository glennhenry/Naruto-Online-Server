package game.mongo.collection

import encore.acts.ActScope
import kotlinx.serialization.Serializable

/**
 * Represents server-wide data.
 *
 * This model contains global server data. It may include both
 * gameplay-related data (e.g., leaderboards, clans) and non-game
 * or operational data (e.g., news, analytics).
 *
 * It's recommended to store only small, bounded data instead of
 * fully-fledged unbounded domain data. For example, this may contains
 * arbitrary metadata, analytics, logs, news, limited global quests, etc;
 * while potentially growing data like is events archive is separated
 * to an independent collection.
 *
 * This object is intended to be a singleton within its collection.
 * There should only be one document of `ServerObjects`, keyed specifically
 * with [ServerObjectsId] in the `ServerObjects` collection.
 *
 * @property dbId Unique identifier. Since only one instance of `ServerObjects`
 *                exists, this acts as a fixed key.
 * @property extra Miscellaneous key-value pair data. This is used to hold
 *                 small data owned by server temporarily.
 */
@Serializable
data class ServerObjects(
    val dbId: String = ServerObjectsId,
    val extra: Map<String, String> = emptyMap(),
)

const val ServerObjectsId = "sobjs"

/**
 * Represent the **one and only** unique identifier for the server.
 *
 * This is used for identification in things like:
 * - [ActScope.ownerId]
 */
const val ServerId = "Xiaoting <33"
