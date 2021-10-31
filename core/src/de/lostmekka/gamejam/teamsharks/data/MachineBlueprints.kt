package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition
import kotlin.math.pow

class MachineBlueprint(
    val name: String,
    val cost: Int,
    val createFunction: (position: GridPosition) -> Machine,
)

val machineBlueprints: Map<MachineType, List<MachineBlueprint>> = mapOf(
    MachineType.Smelter to (1..5).map { tier ->
        MachineBlueprint(
            name = "Smelter",
            cost = 10 * 5f.pow(tier - 1).toInt(),
            createFunction = { pos ->
                Machine(
                    machineType = MachineType.Smelter,
                    position = pos,
                    name = "Smelter",
                    recipes = listOf(
                        Recipe(
                            consumedResources = listOf(2 * ResourceType.IronOre),
                            producedResources = listOf(1 * ResourceType.IronIngot),
                            baseDuration = 10f,
                        ),
                        Recipe(
                            consumedResources = listOf(2 * ResourceType.CopperOre),
                            producedResources = listOf(1 * ResourceType.CopperIngot),
                            baseDuration = 10f,
                        ),
                    ),
                    tier = tier,
                    speedModifier = tier.toFloat(),
                )
            }
        )
    }
)
