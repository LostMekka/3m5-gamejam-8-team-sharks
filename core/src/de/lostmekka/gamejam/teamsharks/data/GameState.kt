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
    val currentResourceDeposits = mutableListOf<ResourceDeposit>()

    fun sell(resourceType: ResourceType, amount: Int) {
        if (factory[resourceType] >= amount) factory -= amount * resourceType
    }

    fun update(deltaTime: Float) {
        // TODO: create new resource deposits as factory descents
        factory.update(deltaTime, currentResourceDeposits)
    }
}
