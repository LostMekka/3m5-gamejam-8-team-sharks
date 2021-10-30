package de.lostmekka.gamejam.teamsharks.data

class Factory {
    val inventory = mutableMapOf<ResourceType, Int>().withDefault { 0 }
    private val machines: List<Machine> = listOf()

    fun update() {
        machines.forEach {
            if (it.update() && inventory[it.consumableResource] == it.amountToConsume) {
                inventory[it.consumableResource]?.minus(it.amountToConsume)
                inventory[it.producibleResource]?.plus(it.amountToProduce) ?: it.amountToProduce
            }
        }
    }

    fun upgrade(fromType: ItemType, toType: ItemType) {
        
    }
}