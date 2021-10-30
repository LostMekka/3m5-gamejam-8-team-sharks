package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition

class GameState {
    var money = 0
    var enemyAwareness = 0f
    val factory = Factory()
    val resourcePrices = mutableMapOf(
        ResourceType.IronOre to 1,
        ResourceType.CopperOre to 2,
        ResourceType.IronIngot to 3,
        ResourceType.CopperIngot to 4,
    )
    val currentResourceDeposits = mutableListOf<ResourceDeposit>()

    fun sellResource(resourceType: ResourceType, amount: Int) {
        if (factory[resourceType] >= amount) {
            factory -= amount * resourceType
            money += resourceType.ordinal
        }
    }

    fun upgradeMachine(gridPosition: GridPosition) {
        val cost = 10 // TODO
        if (money < cost) return
        val machine = factory[gridPosition] ?: return
        machine.upgrade()
        money -= cost
    }

    fun buyMachine(gridPosition: GridPosition, machine: Machine) {
        val cost = 10 // TODO
        if (money < cost) return
        if (factory[gridPosition] != null) return
        factory[gridPosition] = machine
        money -= cost
    }

    fun update(deltaTime: Float) {
        // TODO: create new resource deposits as factory descents
        factory.update(deltaTime, currentResourceDeposits)
    }
}
