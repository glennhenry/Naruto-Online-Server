package encore.context

import encore.network.transport.Connection
import encore.subunit.scope.ServerScope
import game.core.context.PlayerContext
import game.core.context.ServerContext
import game.core.mongo.collection.PlayerId
import kotlinx.coroutines.CoroutineScope

/**
 * Represent a factory responsible for context objects creation such as
 * [PlayerContext] and [ServerContext].
 */
interface ContextFactory {
    /**
     * Create [PlayerContext] for [playerId].
     *
     * @param playerId Unique identifier of the player.
     * @param connection [Connection] object.
     * @param serverContext [ServerContext] object.
     */
    suspend fun playerContext(playerId: PlayerId, connection: Connection, serverContext: ServerContext): PlayerContext

    /**
     * Create [ServerContext].
     *
     * @param appScope The application root coroutine scope.
     * @param serverSubunitScope Server subunit scope.
     */
    suspend fun serverContext(
        appScope: CoroutineScope,
        serverSubunitScope: ServerScope
    ): ServerContext
}
