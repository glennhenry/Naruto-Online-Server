package encore.acts.template

import encore.acts.ActConcept
import encore.acts.StageAct
import encore.acts.choreo.BasicChoreography
import encore.acts.choreo.Choreography
import encore.acts.choreo.PerformMode
import encore.time.TimeCenter
import kotlin.time.Duration

/**
 * A built-in stage act for simple forever timers.
 *
 * This will run after the initial delay of [ForeverTimerConcept.initialDelay].
 * and will perform repeatedly forever between interval of [ForeverTimerConcept.interval].
 */
class ForeverTimerAct : StageAct<ForeverTimerConcept> {
    override val logScheduling: Boolean = false

    override fun choreography(concept: ForeverTimerConcept): Choreography<ForeverTimerConcept> {
        return BasicChoreography(
            initialDelay = concept.initialDelay,
            performMode = PerformMode.Forever(concept.interval)
        )
    }

    override fun metadata(concept: ForeverTimerConcept): Map<String, String> {
        val initialDelay = TimeCenter.formatToDurationString(concept.initialDelay)
        val interval = TimeCenter.formatToDurationString(concept.interval)
        return mapOf(
            "explain" to "Forever timer run after $initialDelay, then every $interval."
        ).plus(
            if (concept.description != null) {
                mapOf("description" to concept.description)
            } else {
                emptyMap()
            }
        )
    }

    override suspend fun perform(concept: ForeverTimerConcept, performNumber: Int) {
        concept.onPerform(performNumber)
    }
}

/**
 * Input for [ForeverTimerAct].
 *
 * @property initialDelay The initial time to wait before first execution.
 * @property interval Time to wait on between each repetition.
 * @property description An optional description on what is this timer used for.
 * @property onPerform Block of code to run on each execution.
 */
data class ForeverTimerConcept(
    val initialDelay: Duration,
    val interval: Duration,
    val description: String? = null,
    val onPerform: suspend (Int) -> Unit
) : ActConcept
