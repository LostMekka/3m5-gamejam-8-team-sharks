package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition
import de.lostmekka.gamejam.teamsharks.util.Progress

class Machine(
    val itemType: ItemType,
    val position: GridPosition,
    var name: String,
    var consumableResource: ResourceType,
    var producibleResource: ResourceType,
    var amountToConsume: Int,
    var amountToProduce: Int,
    var progressDuration: Float,
) {
    private val progress = Progress(progressDuration)

    fun update(timePassed: Float): Boolean {
        progress.increment(timePassed)
        return progress.isMax()
    }

    fun upgrade(newMachine: Machine) {
        name = newMachine.name
        consumableResource = newMachine.consumableResource
        producibleResource = newMachine.producibleResource
        amountToConsume = newMachine.amountToConsume
        amountToProduce = newMachine.amountToProduce
        progressDuration = newMachine.progressDuration
        progress.updateTime = progressDuration
        progress.reset()
    }
}