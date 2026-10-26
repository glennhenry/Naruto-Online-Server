package game.context

import encore.account.AccountRepository
import encore.account.AccountSubunit
import encore.account.BlankAccountRepository
import encore.creation.PlayerCreationSubunit
import encore.presence.PlayerPresenceSubunit
import encore.acts.ActIdStore
import encore.acts.StageActDirector
import encore.auth.AuthSubunit
import encore.backstage.command.CommandDispatcher
import encore.context.ContextFactory
import encore.context.ContextRegistry
import encore.context.FakeContextFactory
import encore.datastore.BlankDataStore
import encore.datastore.DataStore
import encore.extra.player.InMemoryPlayerExtraRepository
import encore.extra.player.PlayerExtraRepository
import encore.extra.player.PlayerExtraSubunit
import encore.extra.server.InMemoryServerExtraRepository
import encore.extra.server.ServerExtraRepository
import encore.extra.server.ServerExtraSubunit
import encore.fancam.Fancam
import encore.network.lifecycle.PlayerLifecycleHandler
import encore.session.SessionSubunit
import encore.subunit.Subunit
import encore.subunit.SubunitLocator
import encore.subunit.scope.ServerScope
import encore.time.source.SystemTimeSource
import encore.time.source.TimeSource
import encore.utils.support.className
import encore.websocket.WebSocketManager
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Represents the **global server-side context**.
 *
 * `ServerContext` includes various server-side components needed on the server.
 * It acts as a dependency container which is distributed across the server code.
 *
 * @property appScope Root coroutine scope system components can use.
 * @property dataStore [DataStore] instance of the server.
 * @property contextRegistry Tracks and manages [PlayerContext].
 * @property commandDispatcher Tracks and executes server commands.
 * @property playerLifecycleHandler Dispatches hook for player's connection activity.
 * @property stageActDirector Provide API to start and stop stage acts.
 * @property webSocketManager Manages client websocket connections.
 * @property subunits Container for server subunit instances.
 * @property locator Component to store subunits.
 */
data class ServerContext(
    val appScope: CoroutineScope,
    val dataStore: DataStore,
    val contextRegistry: ContextRegistry,
    val commandDispatcher: CommandDispatcher,
    val playerLifecycleHandler: PlayerLifecycleHandler,
    val stageActDirector: StageActDirector,
    val webSocketManager: WebSocketManager,
    val subunits: ServerSubunits,
    val locator: SubunitLocator
) {
    companion object {
        /**
         * Creates a test instance of [ServerContext].
         *
         * @param parentScope `CoroutineScope` for [SessionSubunit].
         * @param timeSource [TimeSource] for [StageActDirector].
         * @param dataStore Also used to build [PlayerCreationSubunit].
         * @param playerLifecycleHandler
         * @param accountRepository Used to build [AccountSubunit].
         * @param playerExtraRepository Used to build [PlayerExtraSubunit].
         * @param serverExtraRepository Used to build [ServerExtraSubunit].
         * @param contextFactory Required by [ContextRegistry],
         *                       uses [FakeContextFactory] with empty map by default.
         */
        fun createForTest(
            parentScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
            timeSource: TimeSource = SystemTimeSource(),
            dataStore: DataStore = BlankDataStore(),
            playerLifecycleHandler: PlayerLifecycleHandler = PlayerLifecycleHandler(),
            accountRepository: AccountRepository = BlankAccountRepository(),
            playerExtraRepository: PlayerExtraRepository = InMemoryPlayerExtraRepository(),
            serverExtraRepository: ServerExtraRepository = InMemoryServerExtraRepository(),
            contextFactory: ContextFactory = FakeContextFactory(emptyMap())
        ): ServerContext {
            val account = AccountSubunit(accountRepository)
            val session = SessionSubunit.createForTest(parentScope)
            val creation = PlayerCreationSubunit.createForTest(dataStore)
            val extraPlayer = PlayerExtraSubunit.createForTest(playerExtraRepository)
            val extraServer = ServerExtraSubunit.createForTest(serverExtraRepository)

            return ServerContext(
                appScope = parentScope,
                dataStore = dataStore,
                contextRegistry = ContextRegistry(contextFactory),
                commandDispatcher = CommandDispatcher(),
                playerLifecycleHandler = playerLifecycleHandler,
                stageActDirector = StageActDirector(timeSource, ActIdStore),
                webSocketManager = WebSocketManager(),
                subunits = ServerSubunits(
                    account = account,
                    auth = AuthSubunit(account, creation, session),
                    creation = creation,
                    extraPlayer = extraPlayer,
                    extraServer = extraServer,
                    presence = PlayerPresenceSubunit(),
                    session = session
                ),
                locator = SubunitLocator(),
            )
        }
    }
}

/**
 * Container for all server-scoped [Subunit] instances.
 *
 * Server subunits encapsulate domain logic that operates at the server level.
 * They may manage shared state or provide global domain functionality,
 * with or without persistent data.
 *
 * Server subunits are typically bound to [ServerScope].
 *
 * Examples:
 * - An infra-related component providing session creation and verification.
 * - A leaderboard representing global state is not owned by any single player.
 *   A `LeaderboardSubunit` may expose operations to query or update rankings.
 * - A matchmaking system may not persist data, but can maintain in-memory
 *   state and provide matchmaking-specific functionality.
 *
 * @property account Provides API related to accounts.
 * @property auth Provides authentication functions.
 * @property creation Provides player creation mechanism.
 * @property extraPlayer Provides player extra data service.
 * @property extraServer Provides server extra data service.
 * @property presence Tracks player's presence.
 * @property session Manages session of players.
 */
data class ServerSubunits(
    val account: AccountSubunit,
    val auth: AuthSubunit,
    val creation: PlayerCreationSubunit,
    val extraPlayer: PlayerExtraSubunit,
    val extraServer: ServerExtraSubunit,
    val presence: PlayerPresenceSubunit,
    val session: SessionSubunit,
) {
    /**
     * Return all server subunit instances.
     * **ADD YOUR NEW SUBUNIT HERE TO BE DEBUTED**
     */
    fun all(): Set<Subunit<ServerScope>> {
        return setOf(account, auth, creation, extraPlayer, extraServer, presence, session)
    }

    /**
     * Debut every server subunit instances with [scope].
     */
    suspend fun debut(scope: ServerScope) {
        all().forEach { subunit ->
            val result = subunit.debut(scope)
            if (result.isFailure) {
                Fancam.error(result.exceptionOrNull()) { "Result.failure on ServerSubunit debut '${subunit.className()}'" }
            }
        }
    }

    /**
     * Disband every server subunit instances with [scope].
     */
    suspend fun disband(scope: ServerScope) {
        all().forEach { subunit ->
            val result = subunit.disband(scope)
            if (result.isFailure) {
                Fancam.error(result.exceptionOrNull()) { "Result.failure on ServerSubunit disband '${subunit.className()}'" }
            }
        }
    }
}
