package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition

class Factory {
    private val inventory = mutableMapOf<ResourceType, Int>().withDefault { 0 }
    private val machines = mutableMapOf<GridPosition, Machine>()
    var depth = 0f
    var speed = 0.1f

    fun update(deltaTime: Float) {
        machines.values.forEach {
            if (it.update(deltaTime) && inventory[it.consumableResource] == it.amountConsumed) {
                inventory[it.consumableResource]?.minus(it.amountConsumed)
                inventory[it.producibleResource]?.plus(it.amountProduced) ?: it.amountProduced
            }
        }
        depth += speed
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

    fun proceedLayer(layer: SoilLayer) {
        machines.values.find { it.itemType == ItemType.OreCrusher }?.let { crusher ->
            if (crusher.isReady()) {
                layer.resources.forEach { resource ->
                    inventory[resource] = inventory[resource]
                        ?.plus(crusher.amountProduced)
                        ?: crusher.amountProduced
                }
            }
        }
    }
}
