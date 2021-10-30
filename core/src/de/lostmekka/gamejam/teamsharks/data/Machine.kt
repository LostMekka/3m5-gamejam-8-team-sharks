package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition
import de.lostmekka.gamejam.teamsharks.util.Timer

class Machine(
    val itemType: ItemType,
    val position: GridPosition,
    var name: String,
    val consumedResources: ResourceAmount,
    val producedResources: ResourceAmount,
    var workDuration: Float,
    var tier: Int,
) {
    private val namePrefix = listOf(
        "Better",
        "Upgraded",
        "Super",
        "Duper",
        "Puper",
        "Epic",
        "Mythical",
        "Legendary",
    )

    operator fun get(name: String): String =
        (namePrefix[tier % namePrefix.size] + ' ').repeat(tier / namePrefix.size) + name

    public var isWorking = false
        private set

    public val progress get() = timer.progress

    private val timer = Timer(workDuration)

    fun update(deltaTime: Float, factory: Factory) {
        if (!isWorking && consumedResources in factory) {
            factory -= consumedResources
            isWorking = true
        }
        if (isWorking) {
            var batchesFinished = timer.increment(deltaTime)
            while (batchesFinished > 0) {
                batchesFinished--
                factory += producedResources
                if (consumedResources in factory) {
                    factory -= consumedResources
                } else {
                    isWorking = false
                    break
                }
            }
        }
    }

    fun upgrade() {
        tier++
        workDuration *= 0.9f
        timer.updateTime = workDuration
    }
}
