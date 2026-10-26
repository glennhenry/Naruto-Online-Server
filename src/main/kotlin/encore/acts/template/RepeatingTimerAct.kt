package encore.acts.template

import encore.acts.ActConcept
import encore.acts.StageAct
import encore.acts.choreo.BasicChoreography
import encore.acts.choreo.Choreography
import encore.acts.choreo.PerformMode
import encore.time.TimeCenter
import kotlin.time.Duration

/**
 * A built-in stage act for simple repeating timers.
 *
 * This will run after the initial delay of [RepeatingTimerConcept.initialDelay].
 * and will perform repeatedly for [RepeatingTimerConcept.repetition] between interval of
 * [RepeatingTimerConcept.interval].
 */
class RepeatingTimerAct : StageAct<RepeatingTimerConcept> {
    override val logScheduling: Boolean = false

    override fun choreography(concept: RepeatingTimerConcept): Choreography<RepeatingTimerConcept> {
        return BasicChoreography(
            initialDelay = concept.initialDelay,
            performMode = PerformMode.Repeat(concept.repetition, concept.interval)
        )
    }

    override fun metadata(concept: RepeatingTimerConcept): Map<String, String> {
        val initialDelay = TimeCenter.formatToDurationString(concept.initialDelay)
        val interval = TimeCenter.formatToDurationString(concept.interval)
        return mapOf(
            "explain" to "Repeat timer run after $initialDelay, " +
                    "then every $interval for ${concept.repetition} times."
        ).plus(
            if (concept.description != null) {
                mapOf("description" to concept.description)
            } else {
                emptyMap()
            }
        )
    }

    override suspend fun perform(concept: RepeatingTimerConcept, performNumber: Int) {
        concept.onPerform(performNumber)
    }
}

/**
 * Input for [RepeatingTimerAct].
 *
 * @property initialDelay The initial time to wait before first execution.
 * @property repetition The amount of repetition to do, which excludes the first execution.
 * @property interval Time to wait on between each repetition.
 * @property description An optional description on what is this timer used for.
 * @property onPerform Block of code to run on each execution.
 */
data class RepeatingTimerConcept(
    val initialDelay: Duration,
    val repetition: Int,
    val interval: Duration,
    val description: String? = null,
    val onPerform: suspend (Int) -> Unit
) : ActConcept
