package game.tasks

import kotlinx.serialization.Serializable

@Serializable
data class TaskCountInfo(
   val dailyTaskTotalTimes: UInt,
   val dailyTaskCompleteTimes: UInt,
   val practiceTotalAcceptTimes: UInt,
   val practiceAcceptTimes: UInt,
   val escortTotalAcceptTimes: UInt,
   val escortAcceptTimes: UInt,
   val escortFreeFreshTimes: UInt,
   val escortRefreshTime: UInt,
   val escortFailTime: UInt,
   val escortRealMoney: UInt,
   val wantedTotalAcceptTimes: UInt,
   val wantedAcceptTimes: UInt,
   val wantedTeamExpAddition: UInt,
   val escortRealCoupon: UInt,
   val escortSsFreeAcceptCount: UInt,
   val escortSsTotalFreeAcceptCount: UInt,
   val zhenBaoAddition: UInt,
   val escortRealContri: UInt,
   val weeklyRefreshTime: UInt,
   val activeAwardStat: UInt,
   val awardPackageId: UInt,
   val weeklyTaskFinishCount: UInt,
   val arenaTaskDayCompleteCount: UInt,
   val arenaTaskDayDownCount: UInt,
) {
    companion object {
        fun dummy(): TaskCountInfo {
            return TaskCountInfo(
                dailyTaskTotalTimes = 1u,
                dailyTaskCompleteTimes = 1u,
                practiceTotalAcceptTimes = 1u,
                practiceAcceptTimes = 1u,
                escortTotalAcceptTimes = 1u,
                escortAcceptTimes = 1u,
                escortFreeFreshTimes = 1u,
                escortRefreshTime = 1u,
                escortFailTime = 1u,
                escortRealMoney = 1u,
                wantedTotalAcceptTimes = 1u,
                wantedAcceptTimes = 1u,
                wantedTeamExpAddition = 1u,
                escortRealCoupon = 1u,
                escortSsFreeAcceptCount = 1u,
                escortSsTotalFreeAcceptCount = 1u,
                zhenBaoAddition = 1u,
                escortRealContri = 1u,
                weeklyRefreshTime = 1u,
                activeAwardStat = 1u,
                awardPackageId = 1u,
                weeklyTaskFinishCount = 1u,
                arenaTaskDayCompleteCount = 1u,
                arenaTaskDayDownCount = 1u
            )
        }
    }
}
