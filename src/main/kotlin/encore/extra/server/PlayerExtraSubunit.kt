package encore.extra.server

import encore.datastore.DocumentNotFoundException
import encore.fancam.Fancam
import encore.fancam.Tags
import encore.subunit.Subunit
import encore.subunit.scope.ServerScope
import encore.utils.types.Outcome
import encore.utils.types.Report
import encore.utils.types.toOutcome
import encore.utils.types.toReport

/**
 * Server subunit for [ServerExtraRepository].
 *
 * Server extras are used to hold arbitrary server-data temporarily.
 * It is used to hold small data that doesn't justify its own repository.
 */
class ServerExtraSubunit(
    private val extraRepository: ServerExtraRepository
) : Subunit<ServerScope> {
    /**
     * Retrieve the extra data of [key].
     *
     * Returns:
     * - [Outcome.Fail] if there is an internal repository error.
     * - [Outcome.Ok] with `null` if key is not found.
     * - Otherwise [Outcome.Ok] with the value.
     */
    suspend fun getExtra(key: String): Outcome<String?> {
        return extraRepository.getExtra(key)
            .onFailure {
                if (it is DocumentNotFoundException) {
                    Fancam.error(tag = Tags.ExtraServer) {
                        "getExtra failed: key '$key' not found"
                    }
                } else {
                    Fancam.error(it, Tags.ExtraServer) {
                        "getExtra failed: repository scandal on key=$key"
                    }
                }
            }
            .toOutcome { ext -> return Outcome.Ok(ext) }
    }

    /**
     * Retrieve all extra data.
     *
     * Returns:
     * - [Outcome.Fail] if there is an internal repository error.
     * - [Outcome.Ok] with map of each key to the value.
     */
    suspend fun getAllExtra(): Outcome<Map<String, String>> {
        return extraRepository.getAllExtra()
            .onFailure {
                if (it is DocumentNotFoundException) {
                    Fancam.error(tag = Tags.ExtraServer) {
                        "getAllExtra failed: not found"
                    }
                } else {
                    Fancam.error(it, Tags.ExtraServer) {
                        "getAllExtra failed: repository scandal"
                    }
                }
            }
            .toOutcome { ext -> return Outcome.Ok(ext) }
    }

    /**
     * Update the extra data of [key] to [value].
     *
     * Returns:
     * - [Report.Fail] if there is an internal repository error.
     * - Otherwise [Report.Ok].
     */
    suspend fun updateExtra(key: String, value: String): Report {
        return extraRepository.updateExtra(key, value)
            .onFailure {
                Fancam.error(it, Tags.ExtraServer) {
                    "updateExtra failed: repository scandal on key=$key to value=$value"
                }
            }
            .toReport()
    }

    /**
     * Delete the extra data of [key].
     *
     * Returns:
     * - [Report.Fail] if there is an internal repository error.
     * - Otherwise [Report.Ok].
     */
    suspend fun deleteExtra(key: String): Report {
        return extraRepository.deleteExtra(key)
            .onFailure {
                Fancam.error(it, Tags.ExtraServer) {
                    "deleteExtra failed: repository scandal on key=$key"
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
        fun createForTest(extraRepository: ServerExtraRepository): ServerExtraSubunit {
            return ServerExtraSubunit(extraRepository)
        }
    }
}
