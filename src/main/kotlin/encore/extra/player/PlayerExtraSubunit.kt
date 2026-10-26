package encore.extra.player

import encore.datastore.DocumentNotFoundException
import encore.fancam.Fancam
import encore.fancam.Tags
import encore.subunit.Subunit
import encore.subunit.scope.ServerScope
import encore.utils.types.Outcome
import encore.utils.types.Report
import encore.utils.types.toOutcome
import encore.utils.types.toReport
import game.mongo.collection.PlayerId

/**
 * Server subunit for [PlayerExtraRepository].
 */
class PlayerExtraSubunit(
    private val extraRepository: PlayerExtraRepository
) : Subunit<ServerScope> {
    /**
     * Retrieve the extra data of [key] owned by [playerId].
     *
     * Returns:
     * - [Outcome.Fail] if there is an internal repository error.
     * - [Outcome.Ok] with `null` if key is not found.
     * - Otherwise [Outcome.Ok] with the value.
     */
    suspend fun getExtra(playerId: PlayerId, key: String): Outcome<String?> {
        return extraRepository.getExtra(playerId, key)
            .onFailure {
                if (it is DocumentNotFoundException) {
                    Fancam.error(tag = Tags.ExtraPlayer) {
                        "getExtra failed: key '$key' not found for '$playerId'"
                    }
                } else {
                    Fancam.error(it, Tags.ExtraPlayer) {
                        "getExtra failed: repository scandal for '$playerId' on key=$key"
                    }
                }
            }
            .toOutcome { ext -> return Outcome.Ok(ext) }
    }

    /**
     * Retrieve all extra data owned by [playerId].
     *
     * Returns:
     * - [Outcome.Fail] if there is an internal repository error.
     * - [Outcome.Ok] with map of each key to the value.
     */
    suspend fun getAllExtra(playerId: PlayerId): Outcome<Map<String, String>> {
        return extraRepository.getAllExtra(playerId)
            .onFailure {
                if (it is DocumentNotFoundException) {
                    Fancam.error(tag = Tags.ExtraPlayer) {
                        "getAllExtra failed: not found for '$playerId'"
                    }
                } else {
                    Fancam.error(it, Tags.ExtraPlayer) {
                        "getAllExtra failed: repository scandal for '$playerId'"
                    }
                }
            }
            .toOutcome { ext -> return Outcome.Ok(ext) }
    }

    /**
     * Update the extra data of [key] owned by [playerId] to [value].
     *
     * Returns:
     * - [Report.Fail] if there is an internal repository error.
     * - Otherwise [Report.Ok].
     */
    suspend fun updateExtra(playerId: PlayerId, key: String, value: String): Report {
        return extraRepository.updateExtra(playerId, key, value)
            .onFailure {
                Fancam.error(it, Tags.ExtraPlayer) {
                    "updateExtra failed: repository scandal for '$playerId' on key=$key to value=$value"
                }
            }
            .toReport()
    }

    /**
     * Delete the extra data of [key] owned by [playerId].
     *
     * Returns:
     * - [Report.Fail] if there is an internal repository error.
     * - Otherwise [Report.Ok].
     */
    suspend fun deleteExtra(playerId: PlayerId, key: String): Report {
        return extraRepository.deleteExtra(playerId, key)
            .onFailure {
                Fancam.error(it, Tags.ExtraPlayer) {
                    "deleteExtra failed: repository scandal for '$playerId' on key=$key"
                }
            }
            .toReport()
    }

    override suspend fun debut(scope: ServerScope): Result<Unit> {
        return runCatching { }
    }

    override suspend fun disband(scope: ServerScope): Result<Unit> {
        return runCatching { }
    }

    companion object {
        fun createForTest(extraRepository: PlayerExtraRepository): PlayerExtraSubunit {
            return PlayerExtraSubunit(extraRepository)
        }
    }
}
