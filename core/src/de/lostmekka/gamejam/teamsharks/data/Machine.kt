package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition
import de.lostmekka.gamejam.teamsharks.util.Timer

class Machine(
    val itemType: ItemType,
    val position: GridPosition,
    val name: String,
    val consumedResources: ResourceAmount,
    val producedResources: ResourceAmount,
    val workDuration: Float,
) {
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
}
