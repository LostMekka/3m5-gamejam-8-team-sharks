package de.lostmekka.gamejam.teamsharks.data

import com.badlogic.gdx.graphics.Color
import de.lostmekka.gamejam.teamsharks.data.GameConstants.dirtLayerColorVariance
import de.lostmekka.gamejam.teamsharks.data.GameConstants.dirtLayerScale
import de.lostmekka.gamejam.teamsharks.data.GameConstants.resourcePrices
import de.lostmekka.gamejam.teamsharks.util.GridPosition
import kotlin.random.Random

class GameState {
    var money = 0
    var enemyAwareness = 0f
    val factory = Factory()
    val currentResourceDeposits = mutableListOf<ResourceDeposit>()
    var dirtLayerOffset = 0f

    fun update(deltaTime: Float) {
        // TODO: create new resource deposits as factory descents
        factory.update(deltaTime, currentResourceDeposits)
        dirtLayerOffset = (dirtLayerOffset + deltaTime * factory.drillingSpeed) % (dirtLayerScale)
    }

    private fun randomTintColor(): Color {
        val r = Random.nextFloat() * dirtLayerColorVariance + 1f - dirtLayerColorVariance
        return Color(r, r, r, 1f)
    }

    fun sellResource(resourceAmount: ResourceAmount) {
        val (type, amount) = resourceAmount
        if (factory[type] >= amount) {
            factory -= amount * type
            money += amount * resourcePrices.getOrDefault(type, 1)
        }
    }

    fun upgradeMachine(gridPosition: GridPosition, blueprint: MachineBlueprint) {
        val cost = blueprint.cost
        if (money < cost) return
        factory[gridPosition] ?: return
        factory[gridPosition] = blueprint.createFunction(gridPosition)
        money -= cost
    }

    fun buyMachine(gridPosition: GridPosition, blueprint: MachineBlueprint) {
        val cost = blueprint.cost
        if (money < cost) return
        if (factory[gridPosition] != null) return
        factory[gridPosition] = blueprint.createFunction(gridPosition)
        money -= cost
    }
}
