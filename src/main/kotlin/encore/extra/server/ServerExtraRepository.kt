package encore.extra.server

import game.mongo.collection.ServerObjects

/**
 * Repository handling [ServerObjects.extra] concern.
 *
 * Server extras are used to hold arbitrary server-data temporarily.
 * It is used to hold small data that doesn't justify its own repository.
 */
interface ServerExtraRepository {
    /**
     * Retrieve the extra data of [key].
     * @return [Result.failure] for any repository error,
     *         otherwise [Result.success] with the value.
     */
    suspend fun getExtra(key: String): Result<String>

    /**
     * Retrieve all extra data.
     * @return [Result.failure] for any repository error,
     *         otherwise [Result.success] with the value.
     */
    suspend fun getAllExtra(): Result<Map<String, String>>

    /**
     * Update the extra data of [key] by [value].
     * @return [Result.failure] for any repository error, otherwise [Result.success].
     */
    suspend fun updateExtra(key: String, value: String): Result<Unit>

    /**
     * Delete the extra data of [key].
     * @return [Result.failure] for any repository error, otherwise [Result.success].
     */
    suspend fun deleteExtra(key: String): Result<Unit>
}
