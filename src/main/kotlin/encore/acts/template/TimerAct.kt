package encore.acts.template

import encore.acts.ActConcept
import encore.acts.StageAct
import encore.acts.choreo.BasicChoreography
import encore.acts.choreo.Choreography
import encore.acts.choreo.PerformMode
import encore.time.TimeCenter
import kotlin.time.Duration

/**
 * A built-in stage act for simple timers.
 *
 * This will run only once after the given time from [TimerConcept.time].
 */
class TimerAct : StageAct<TimerConcept> {
    override val logScheduling: Boolean = false

    override fun choreography(concept: TimerConcept): Choreography<TimerConcept> {
        return BasicChoreography(
            initialDelay = concept.time,
            performMode = PerformMode.Once
        )
    }

    override fun metadata(concept: TimerConcept): Map<String, String> {
        return mapOf(
            "explain" to "Timer run after ${TimeCenter.formatToDurationString(concept.time)}."
        ).plus(
            if (concept.description != null) {
                mapOf("description" to concept.description)
            } else {
                emptyMap()
            }
        )
    }

    override suspend fun perform(concept: TimerConcept, performNumber: Int) {
        concept.onPerform(performNumber)
    }
}

/**
 * Input for [TimerAct].
 *
 * @property time The amount of time to wait before execution.
 * @property description An optional description on what is this timer used for.
 * @property onPerform Block of code to run after waiting the amount of [time].
 */
data class TimerConcept(
    val time: Duration,
    val description: String? = null,
    val onPerform: suspend (Int) -> Unit
) : ActConcept
