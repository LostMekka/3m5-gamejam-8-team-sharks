package de.lostmekka.gamejam.teamsharks.data

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
            resourceAmount = (Random.nextFloat() * (depth * 2f + 100f) + 100f).roundToInt(),
        )
    },
    ResourceType.CopperOre to ResourceDepositBlueprint { depth ->
        ResourceDeposit(
            depth = depth,
            height = 96f,
            resourceType = ResourceType.CopperOre,
            resourceAmount = (Random.nextFloat() * (depth * 2f + 100f) + 100f).roundToInt(),
        )
    },
    ResourceType.Coal to ResourceDepositBlueprint { depth ->
        ResourceDeposit(
            depth = depth,
            height = 96f,
            resourceType = ResourceType.Coal,
            resourceAmount = (Random.nextFloat() * (depth * 2f + 100f) + 100f).roundToInt(),
        )
    },
)
