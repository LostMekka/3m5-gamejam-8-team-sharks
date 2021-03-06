package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridSection
import de.lostmekka.gamejam.teamsharks.util.by

object GameConstants {
    val gridSize = 10 by 10
    val borderSize = 2 by 2
    val grid = GridSection(0, 0, gridSize)
    val inventorySpace = GridSection(0, 5, 3 by 5)

    val dirtLayerScale = 64
    val dirtLayerColorVariance = 0.2f

    val machineScale = 99

    val factoryWidth = 1536
    val factoryHeight = 864

    val factoryBaseMiningSpeed = 1f
    val factoryBaseDrillingSpeed = 5f
    val factoryBaseAwarenessMultiplier = 0.001f

    val bribeCostPerDepth = 0.05f

    val resourcePrices = mutableMapOf(
        ResourceType.IronOre to 1,
        ResourceType.IronIngot to 5,
        ResourceType.CopperOre to 1,
        ResourceType.CopperIngot to 5,
        ResourceType.CopperWire to 5,
        ResourceType.Coal to 1,
        ResourceType.Oil to 15,
        ResourceType.SteelBeam to 50,
    )
}
