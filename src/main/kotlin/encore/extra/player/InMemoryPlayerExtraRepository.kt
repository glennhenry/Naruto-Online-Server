package encore.extra.player

import encore.datastore.DocumentNotFoundException
import game.mongo.collection.PlayerId

class InMemoryPlayerExtraRepository(
    private val initialMap: MutableMap<String, MutableMap<String, String>> = mutableMapOf()
) : PlayerExtraRepository {
    override suspend fun getExtra(
        playerId: PlayerId,
        key: String
    ): Result<String> {
        if (initialMap[playerId] == null) {
            return Result.failure(
                DocumentNotFoundException("playerId '$playerId' not found")
            )
        }
        val value = initialMap[playerId]!![key] ?: return Result.failure(
            DocumentNotFoundException("playerId '$playerId' found but key '$key' not found")
        )
        return Result.success(value)
    }

    override suspend fun getAllExtra(
        playerId: PlayerId
    ): Result<Map<String, String>> {
        if (initialMap[playerId] == null) {
            return Result.failure(
                DocumentNotFoundException("playerId '$playerId' not found")
            )
        }
        return Result.success(initialMap[playerId]!!)
    }

    override suspend fun updateExtra(
        playerId: PlayerId,
        key: String,
        value: String
    ): Result<Unit> {
        initialMap[playerId]?.set(key, value)
        return Result.success(Unit)
    }

    override suspend fun deleteExtra(
        playerId: PlayerId,
        key: String
    ): Result<Unit> {
        initialMap[playerId]?.remove(key)
        return Result.success(Unit)
    }
}
