package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.SoundEventHandler
import de.lostmekka.gamejam.teamsharks.rect
import de.lostmekka.gamejam.teamsharks.util.GridPosition

class Factory {
    private val inventory = mutableMapOf<ResourceType, Int>().withDefault { 0 }
    private val machines = mutableMapOf<GridPosition, Machine>()
    var depth = 0f
    var drillingSpeed = GameConstants.factoryBaseDrillingSpeed
    var miningSpeed = GameConstants.factoryBaseMiningSpeed

    fun update(deltaTime: Float, deposits: List<ResourceDeposit>, soundEventHandler: SoundEventHandler) {
        drillingSpeed = GameConstants.factoryBaseDrillingSpeed + sumOverMachines { drillSpeedBonus }
        miningSpeed = GameConstants.factoryBaseMiningSpeed + sumOverMachines { miningSpeedBonus }

        depth += drillingSpeed * deltaTime
        machines.values.forEach { it.update(deltaTime, this, soundEventHandler) }
        val coveringHeightRange = (depth - GameConstants.grid.rect.height)..depth
        deposits.forEach { this += it.update(deltaTime, miningSpeed, coveringHeightRange) }
    }

    private fun sumOverMachines(block: Machine.() -> Float) =
        machines.values.sumOf { it.block().toDouble() }.toFloat()

    operator fun get(pos: GridPosition) = machines[pos]
    operator fun set(pos: GridPosition, machine: Machine) {
        machines[pos] = machine
    }
    operator fun minusAssign(pos: GridPosition) {
        machines -= pos
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
