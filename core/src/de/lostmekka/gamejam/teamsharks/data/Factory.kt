package de.lostmekka.gamejam.teamsharks.data

class Factory {
    val inventory = mutableMapOf<ResourceType, Int>().withDefault { 0 }
    private val machines = mutableListOf<Machine>()

    fun update(timePassed: Float) {
        machines.forEach {
            if (it.update(timePassed) && inventory[it.consumableResource] == it.amountToConsume) {
                inventory[it.consumableResource]?.minus(it.amountToConsume)
                inventory[it.producibleResource]?.plus(it.amountToProduce) ?: it.amountToProduce
            }
        }
    }

    fun insert(newMachine: Machine) {
        val machine = machines.find { it.itemType == newMachine.itemType }
        machine?.upgrade(newMachine) ?: machines.add(newMachine)
    }
}