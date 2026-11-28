package game.tasks

import game.protocol.response.RetInfo
import kotlinx.serialization.Serializable

@Serializable
data class TaskInfoListResponse(
    val ret: RetInfo = RetInfo(0, ""),
    val tasks: List<TaskInfo>,
    val taskCountInfo: TaskCountInfo
)
