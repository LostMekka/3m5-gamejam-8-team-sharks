package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.GameEventHandler
import de.lostmekka.gamejam.teamsharks.util.GridPosition

class Machine(
    val machineType: MachineType,
    val position: GridPosition,
    val name: String,
    val tier: Int,
    val recipes: List<Recipe> = listOf(),
    val speedModifier: Float = 1f,
    val drillSpeedBonus: Float = 0f,
    val miningSpeedBonus: Float = 0f,
    val awarenessFactorBonus: Float = 0f,
) {
    private val namePrefix = listOf(
        "Better",
        "Upgraded",
        "Super",
        "Duper",
        "Puper",
        "Epic",
        "Mythical",
        "Legendary",
    )

    operator fun get(name: String): String =
        (namePrefix[tier % namePrefix.size] + ' ').repeat(tier / namePrefix.size) + name

    public val isWorking get() = currentRecipe != null
    public var progress = 0f
        private set

    private var currentRecipe: Recipe? = null

    fun update(deltaTime: Float, factory: Factory, gameEventHandler: GameEventHandler) {
        if (currentRecipe == null) startRandomRecipe(factory)
        var timeLeft = deltaTime
        while (currentRecipe != null && timeLeft > 0f) {
            val recipe = currentRecipe!!
            val progressLeft = 1f - progress
            val possibleProgress = timeLeft * speedModifier / recipe.baseDuration
            if (possibleProgress < progressLeft) {
                progress += possibleProgress
                timeLeft = 0f
            } else {
                progress = 0f
                timeLeft -= timeLeft * progressLeft / possibleProgress
                recipe.producedResources.forEach { factory += it }
                startRandomRecipe(factory)
                gameEventHandler.onMachineFinished(machineType)
            }
        }
    }

    fun resumeRecipe(oldMachine: Machine) {
        currentRecipe = oldMachine.currentRecipe
        progress = oldMachine.progress
    }

    private fun startRandomRecipe(factory: Factory) {
        val recipe = recipes
            .filter { recipe -> recipe.consumedResources.all { it in factory } }
            .randomOrNull()
        recipe?.consumedResources?.forEach { factory -= it }
        currentRecipe = recipe
    }
}
