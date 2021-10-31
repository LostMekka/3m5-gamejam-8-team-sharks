package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.data.ResourceType.*
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
                            consumedResources = listOf(2 * IronOre),
                            producedResources = listOf(1 * IronIngot),
                            baseDuration = 10f,
                        ),
                        Recipe(
                            consumedResources = listOf(2 * CopperOre),
                            producedResources = listOf(1 * CopperIngot),
                            baseDuration = 10f,
                        ),
                    ),
                    tier = tier,
                    speedModifier = tier.toFloat(),
                )
            }
        )
    },
    MachineType.Furnace to (1..5).map { tier ->
        MachineBlueprint(
            name = "Furnace",
            cost = 200 * 5f.pow(tier - 1).toInt(),
            createFunction = { pos ->
                Machine(
                    machineType = MachineType.Furnace,
                    position = pos,
                    name = "Furnace",
                    recipes = listOf(
                        Recipe(
                            consumedResources = listOf(2 * Coal, 5 * IronIngot),
                            producedResources = listOf(1 * SteelBeam),
                            baseDuration = 25f,
                        ),
                    ),
                    tier = tier,
                    speedModifier = tier.toFloat(),
                )
            }
        )
    },
    MachineType.WireMaker to (1..5).map { tier ->
        MachineBlueprint(
            name = "Wire Maker",
            cost = 100 * 5f.pow(tier - 1).toInt(),
            createFunction = { pos ->
                Machine(
                    machineType = MachineType.WireMaker,
                    position = pos,
                    name = "Wire Maker",
                    recipes = listOf(
                        Recipe(
                            consumedResources = listOf(1 * CopperIngot),
                            producedResources = listOf(2 * CopperWire),
                            baseDuration = 10f,
                        ),
                    ),
                    tier = tier,
                    speedModifier = tier.toFloat(),
                )
            }
        )
    },
    MachineType.DrillModule to (1..100).map { tier ->
        MachineBlueprint(
            name = "Drill Module",
            cost = 100 * 2f.pow(tier - 1).toInt(),
            createFunction = { pos ->
                Machine(
                    machineType = MachineType.DrillModule,
                    position = pos,
                    name = "Drill Module",
                    recipes = listOf(),
                    tier = tier,
                    drillSpeedPercentageBonus = tier * 2f,
                )
            }
        )
    },
    MachineType.MiningModule to (1..100).map { tier ->
        MachineBlueprint(
            name = "Mining Module",
            cost = 100 * 2f.pow(tier - 1).toInt(),
            createFunction = { pos ->
                Machine(
                    machineType = MachineType.MiningModule,
                    position = pos,
                    name = "Mining Module",
                    tier = tier,
                    miningSpeedPercentageBonus = tier * 2f,
                )
            }
        )
    },
)
