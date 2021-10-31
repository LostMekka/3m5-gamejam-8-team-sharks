package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition
import kotlin.math.roundToInt
import kotlin.random.Random

class ResourceDepositBlueprint(
    val createFunction: (depth: Float) -> ResourceDeposit,
)

val resourceDepositBlueprints: Map<ResourceType, ResourceDepositBlueprint> = mapOf(
    ResourceType.IronOre to ResourceDepositBlueprint { depth ->
        ResourceDeposit(
            depth = depth,
            height = 96f,
            resourceType = ResourceType.IronOre,
            resourceAmount = (Random.nextFloat() * (depth + 100f) + 20f).roundToInt(),
        )
    },
    ResourceType.CopperOre to ResourceDepositBlueprint { depth ->
        ResourceDeposit(
            depth = depth,
            height = 96f,
            resourceType = ResourceType.CopperOre,
            resourceAmount = (Random.nextFloat() * (depth + 100f) + 20f).roundToInt(),
        )
    },
)
