package de.lostmekka.gamejam.teamsharks.data

import com.badlogic.gdx.graphics.Color
import de.lostmekka.gamejam.teamsharks.SoundEventHandler
import de.lostmekka.gamejam.teamsharks.data.GameConstants.dirtLayerColorVariance
import de.lostmekka.gamejam.teamsharks.data.GameConstants.dirtLayerScale
import de.lostmekka.gamejam.teamsharks.data.GameConstants.resourcePrices
import de.lostmekka.gamejam.teamsharks.rect
import de.lostmekka.gamejam.teamsharks.util.GridPosition
import kotlin.random.Random

class GameState {
    var money = 0
    var enemyAwareness = 0f
    val factory = Factory()
    val currentResourceDeposits = mutableListOf<ResourceDeposit>()
    var nextResourceDepositDepth = 0f
    var dirtLayerOffset = 0f

    init {
        currentResourceDeposits += resourceDepositBlueprints.values.random().createFunction(1f)
    }

    fun update(deltaTime: Float, soundEventHandler: SoundEventHandler) {
        factory.update(deltaTime, currentResourceDeposits, soundEventHandler)
        val deltaDepth = deltaTime * factory.drillingSpeed

        val minDepositDepth = factory.depth - GameConstants.grid.rect.height - 500
        currentResourceDeposits.removeAll { it.depth < minDepositDepth }
        if (factory.depth > nextResourceDepositDepth) {
            currentResourceDeposits += resourceDepositBlueprints.values.random().createFunction(factory.depth + 400)
            nextResourceDepositDepth = factory.depth + Random.nextFloat() * 400f + 200f
        }

        dirtLayerOffset = (dirtLayerOffset + deltaDepth) % (dirtLayerScale)
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
