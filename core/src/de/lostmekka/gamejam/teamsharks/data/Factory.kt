package de.lostmekka.gamejam.teamsharks.data

class Factory {
    val inventory = mutableMapOf<ResourceType, Int>().withDefault { 0 }
    private val machines: List<Machine> = listOf()

    fun update(timePassed: Float) {
        machines.forEach {
            if (it.update(timePassed) && inventory[it.consumableResource] == it.amountToConsume) {
                inventory[it.consumableResource]?.minus(it.amountToConsume)
                inventory[it.producibleResource]?.plus(it.amountToProduce) ?: it.amountToProduce
            }
        }
    }
    // TODO: change parameters
    fun upgrade(
        fromType: ItemType,
        name: String,
        consumableResource: ResourceType,
        producibleResource: ResourceType,
        amountToConsume: Int,
        amountToProduce: Int,
        progressDuration: Float,
    ) {
        machines
            .find { it.itemType == fromType }
            ?.upgrade(
                name = name,
                consumableResource = consumableResource,
                producibleResource = producibleResource,
                amountToConsume = amountToConsume,
                amountToProduce = amountToProduce,
                progressDuration = progressDuration,
            )
    }
}