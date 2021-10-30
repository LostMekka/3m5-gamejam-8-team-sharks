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

    fun upgrade(
        name: String,
        consumableResource: ResourceType,
        producibleResource: ResourceType,
        amountToConsume: Int,
        amountToProduce: Int,
        progressDuration: Float,
    ) {
        this.name = name
        this.consumableResource = consumableResource
        this.producibleResource = producibleResource
        this.amountToConsume = amountToConsume
        this.amountToProduce = amountToProduce
        this.progressDuration = progressDuration
        progress.updateTime = progressDuration
        progress.reset()
    }
}