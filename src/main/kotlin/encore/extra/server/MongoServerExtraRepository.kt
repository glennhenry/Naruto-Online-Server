package encore.extra.server

import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import encore.datastore.ServerObjectsFilter
import encore.datastore.runMongoCatching
import encore.datastore.throwIfNothingMatched
import game.mongo.collection.ServerObjects
import kotlinx.coroutines.flow.firstOrNull

class MongoServerExtraRepository(
    private val serverObjects: MongoCollection<ServerObjects>
) : ServerExtraRepository {
    override suspend fun getExtra(key: String): Result<String> {
        return runMongoCatching {
            serverObjects
                .find(ServerObjectsFilter)
                .firstOrNull()
                ?.extra[key]
        }
    }

    override suspend fun getAllExtra(): Result<Map<String, String>> {
        return runMongoCatching {
            serverObjects
                .find(ServerObjectsFilter)
                .firstOrNull()
                ?.extra
        }
    }

    override suspend fun updateExtra(key: String, value: String): Result<Unit> {
        return runMongoCatching {
            val filter = ServerObjectsFilter
            val update = Updates.set("extra.$key", value)
            serverObjects.updateOne(filter, update)
                .throwIfNothingMatched("updateExtra") { filter }
        }
    }

    override suspend fun deleteExtra(key: String): Result<Unit> {
        return runMongoCatching {
            val filter = ServerObjectsFilter
            val update = Updates.unset("extra.$key")
            serverObjects.updateOne(filter, update)
                .throwIfNothingMatched("deleteExtra") { filter }
        }
    }
}
