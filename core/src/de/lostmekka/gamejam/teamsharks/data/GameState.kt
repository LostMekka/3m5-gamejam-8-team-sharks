package de.lostmekka.gamejam.teamsharks.data

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

    fun sell(resourceType: ResourceType, amount: Int) {
        if (factory.inStock(resourceType) >= amount)
            factory.removeFromInventory(resourceType, amount)
    }

    fun update(deltaTime: Float) {
        // TODO: implement
    }
}
