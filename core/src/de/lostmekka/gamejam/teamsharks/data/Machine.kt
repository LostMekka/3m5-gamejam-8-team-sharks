package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition
import de.lostmekka.gamejam.teamsharks.util.Progress

class Machine(
    val name: String,
    val itemType: ItemType,
    val consumableResource: ResourceType,
    val producibleResource: ResourceType,
    val amountToConsume: Int,
    val amountToProduce: Int,
    val position: GridPosition,
) {
    private val progress = Progress()

    fun update(): Boolean {
        progress.increment()
        return progress.isMax()
    }
}