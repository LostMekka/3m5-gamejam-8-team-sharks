package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition

class Factory {
    private val inventory = mutableMapOf<ResourceType, Int>().withDefault { 0 }
    private val machines = mutableMapOf<GridPosition, Machine>()
    var depth = 0f
    var speed = 0.1f
    var miningSpeed = 1f

    fun update(deltaTime: Float, deposits: List<ResourceDeposit>) {
        depth += speed * deltaTime
        machines.values.forEach { it.update(deltaTime, this) }
        val coveringHeightRange = (depth - GameConstants.gridSize.y)..depth
        deposits
            .filter { it touches coveringHeightRange }
            .forEach { this += it.update(deltaTime, miningSpeed) }
    }

    operator fun get(pos: GridPosition) = machines[pos]
    operator fun set(pos: GridPosition, machine: Machine) {
        machines[pos] = machine
    }
    operator fun minusAssign(pos: GridPosition) {
        machines -= pos
    }

    operator fun plusAssign(itemType: ItemType) {
        machines.values.find { it.itemType == itemType }?.upgrade()
    }

    operator fun get(resourceType: ResourceType) = inventory.getValue(resourceType)
    operator fun contains(resourceAmount: ResourceAmount): Boolean {
        val (type, amount) = resourceAmount
        return inventory.getValue(type) >= amount
    }
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
