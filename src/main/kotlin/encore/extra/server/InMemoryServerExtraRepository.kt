package encore.extra.server

import encore.datastore.DocumentNotFoundException

class InMemoryServerExtraRepository(
    private val initialMap: MutableMap<String, String> = mutableMapOf()
) : ServerExtraRepository {
    override suspend fun getExtra(key: String): Result<String> {
        val value = initialMap[key] ?: return Result.failure(DocumentNotFoundException("key '$key' not found"))
        return Result.success(value)
    }

    override suspend fun getAllExtra(): Result<Map<String, String>> {
        return Result.success(initialMap)
    }

    override suspend fun updateExtra(key: String, value: String): Result<Unit> {
        initialMap[key] = value
        return Result.success(Unit)
    }

    override suspend fun deleteExtra(key: String): Result<Unit> {
        initialMap.remove(key)
        return Result.success(Unit)
    }
}
