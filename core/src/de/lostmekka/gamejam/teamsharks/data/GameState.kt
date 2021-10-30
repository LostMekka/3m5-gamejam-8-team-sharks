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
    val currentLayers = mutableListOf<SoilLayer>()

    fun sell(resourceType: ResourceType, amount: Int) {
        if (factory[resourceType] >= amount) factory -= amount * resourceType
    }

    fun update(deltaTime: Float) {
        currentLayers.forEach { soilLayer ->
            if (soilLayer.isColliding(factory.depth.toInt())) //TODO: change to float
                factory.proceedLayer(soilLayer)
        }
        factory.update(deltaTime)
    }
}
