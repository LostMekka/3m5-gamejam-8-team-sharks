package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition

class MachineBlueprint(
    val name: String,
    val cost: Int,
    val createFunction: (position: GridPosition) -> Machine,
)

val machineBlueprints: Map<MachineType, List<MachineBlueprint>> = mapOf(
    MachineType.Smelter to (1..5).map { tier ->
        MachineBlueprint(
            name = "Smelter",
            cost = tier * 10,
            createFunction = { pos ->
                Machine(
                    machineType = MachineType.Smelter,
                    position = pos,
                    name = "Smelter",
                    consumedResources = 2 * ResourceType.IronOre,
                    producedResources = 1 * ResourceType.IronIngot,
                    workDuration = (6f - tier) * 2f,
                    tier = tier,
                )
            }
        )
    }
)
