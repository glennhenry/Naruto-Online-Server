package encoreTest.extra

import TestMongoCollections
import encore.extra.server.MongoServerExtraRepository
import game.mongo.collection.ServerObjects
import initMongo
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MongoServerExtraRepositoryTest {
    @Test
    fun `test all`() = runTest {
        val mongoDb = initMongo()
        val collection = mongoDb.getCollection<ServerObjects>(TestMongoCollections.serverObjects)
        collection.drop()
        mongoDb.createCollection(TestMongoCollections.serverObjects)

        collection.insertOne(
            ServerObjects(
                extra = mapOf("a" to "123", "b" to "456")
            )
        )

        val repo = MongoServerExtraRepository(collection)

        // 1. getExtra
        assertEquals("123", repo.getExtra(key = "a").getOrNull())

        // 2. updateExtra
        assertNotNull(repo.updateExtra(key = "a", value = "444"))
        assertEquals("444", repo.getExtra( key = "a").getOrNull())

        // 3. deleteExtra
        assertNotNull(repo.deleteExtra("a").getOrNull())
        assertEquals(null, repo.getExtra(key = "a").getOrNull())
    }
}
