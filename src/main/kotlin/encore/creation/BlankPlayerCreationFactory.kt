package encore.creation

import encore.utils.types.Report
import game.core.mongo.collection.PlayerAccount
import game.core.mongo.collection.PlayerId
import game.core.mongo.collection.PlayerObjects
import game.core.mongo.collection.PlayerServerObjects

class BlankPlayerCreationFactory: PlayerCreationFactory {
    override fun playerId(isAdmin: Boolean): PlayerId {
        TODO("Not yet implemented")
    }

    override fun account(
        playerId: PlayerId,
        username: String,
        password: String,
        email: String
    ): PlayerAccount {
        TODO("Not yet implemented")
    }

    override fun playerObjects(playerId: PlayerId): PlayerObjects {
        TODO("Not yet implemented")
    }

    override fun playerServerObjects(playerId: PlayerId): PlayerServerObjects {
        TODO("Not yet implemented")
    }

    override fun updateServerObjects(
        account: PlayerAccount,
        objects: PlayerObjects
    ): Report {
        TODO("Not yet implemented")
    }
}
