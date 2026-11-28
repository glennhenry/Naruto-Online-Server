package game.tasks

import kotlinx.serialization.Serializable

@Serializable
data class TaskInfo(
    val id: UInt,
    val type: UInt,
    val state: UInt,
    val isNew: UInt,
    val conditions: List<ConditionInfo>,
    val acceptTime: UInt,
    val condState: UInt,
    val activePoint: Int,
) {
    companion object {
        fun dummy(): TaskInfo {
            return TaskInfo(
                id = 1u,
                type = 1u,
                state = 1u,
                isNew = 1u,
                conditions = emptyList(),
                acceptTime = 1u,
                condState = 1u,
                activePoint = 1
            )
        }
    }
}
