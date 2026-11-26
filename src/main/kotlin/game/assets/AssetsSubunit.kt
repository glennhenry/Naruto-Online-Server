package game.assets

import encore.subunit.Subunit
import encore.subunit.scope.ServerScope
import game.assets.mirror.LocalAssetsIndex

/**
 * Assets subunit
 */
class AssetsSubunit : Subunit<ServerScope> {
//    val local: LocalAssetsIndex = LocalAssetsIndex()

    override suspend fun debut(scope: ServerScope): Result<Unit> {
        return runCatching {
//            local.build()
        }
    }

    override suspend fun disband(scope: ServerScope): Result<Unit> {
        return runCatching { }
    }

    companion object {
        fun createForTest(): AssetsSubunit {
            return AssetsSubunit()
        }
    }
}
