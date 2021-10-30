package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.data.GameConstants.resourcePrices
import de.lostmekka.gamejam.teamsharks.util.GridPosition

class GameState {
    var money = 0
    var enemyAwareness = 0f
    val factory = Factory()
    val currentResourceDeposits = mutableListOf<ResourceDeposit>()

    fun sellResource(resourceAmount: ResourceAmount) {
        val (type, amount) = resourceAmount
        if (factory[type] >= amount) {
            factory -= amount * type
            money += amount * resourcePrices.getOrDefault(type, 1)
        }
    }

    fun upgradeMachine(gridPosition: GridPosition, blueprint: MachineBlueprint) {
        val cost = blueprint.cost
        if (money < cost) return
        factory[gridPosition] ?: return
        factory[gridPosition] = blueprint.createFunction(gridPosition)
        money -= cost
    }

    fun buyMachine(gridPosition: GridPosition, blueprint: MachineBlueprint) {
        val cost = blueprint.cost
        if (money < cost) return
        if (factory[gridPosition] != null) return
        factory[gridPosition] = blueprint.createFunction(gridPosition)
        money -= cost
    }

    fun update(deltaTime: Float) {
        // TODO: create new resource deposits as factory descents
        factory.update(deltaTime, currentResourceDeposits)
    }
}
