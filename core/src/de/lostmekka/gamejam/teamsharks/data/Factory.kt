package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition

class Factory {
    private val inventory = mutableMapOf<ResourceType, Int>().withDefault { 0 }
    private val machines = mutableMapOf<GridPosition, Machine>()

    fun update(deltaTime: Float) {
        machines.values.forEach {
            if (it.update(deltaTime) && inventory[it.consumableResource] == it.amountToConsume) {
                inventory[it.consumableResource]?.minus(it.amountToConsume)
                inventory[it.producibleResource]?.plus(it.amountToProduce) ?: it.amountToProduce
            }
        }
    }

    operator fun get(pos: GridPosition) = machines[pos]
    operator fun set(pos: GridPosition, machine: Machine) {
        machines[pos] = machine
    }
    operator fun minusAssign(pos: GridPosition) {
        machines -= pos
    }

    operator fun get(resourceType: ResourceType) = inventory.getValue(resourceType)
    operator fun plusAssign(resourceAmount: ResourceAmount) {
        val (type, amount) = resourceAmount
        inventory[type] = inventory.getValue(type) + amount
    }
    operator fun minusAssign(resourceAmount: ResourceAmount) {
        val (type, amount) = resourceAmount
        val currAmount = inventory.getValue(type)
        require(currAmount >= amount) { "tried to subtract $amount $type, but inventory only has $currAmount" }
        inventory[type] = currAmount - amount
    }
}
