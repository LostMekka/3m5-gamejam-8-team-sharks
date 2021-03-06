package de.lostmekka.gamejam.teamsharks

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.teamsharks.data.GameConstants.borderSize
import de.lostmekka.gamejam.teamsharks.data.GameConstants.dirtLayerScale
import de.lostmekka.gamejam.teamsharks.data.GameConstants.factoryBaseAwarenessMultiplier
import de.lostmekka.gamejam.teamsharks.data.GameConstants.factoryHeight
import de.lostmekka.gamejam.teamsharks.data.GameConstants.factoryWidth
import de.lostmekka.gamejam.teamsharks.data.GameConstants.grid
import de.lostmekka.gamejam.teamsharks.data.GameConstants.gridSize
import de.lostmekka.gamejam.teamsharks.data.GameConstants.inventorySpace
import de.lostmekka.gamejam.teamsharks.data.GameState
import de.lostmekka.gamejam.teamsharks.data.MachineBlueprint
import de.lostmekka.gamejam.teamsharks.data.MachineType
import de.lostmekka.gamejam.teamsharks.data.ResourceAmount
import de.lostmekka.gamejam.teamsharks.data.ResourceDeposit
import de.lostmekka.gamejam.teamsharks.data.times
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import de.lostmekka.gamejam.teamsharks.data.machineBlueprints
import de.lostmekka.gamejam.teamsharks.helper.draw
import de.lostmekka.gamejam.teamsharks.helper.ifKeyPressed
import de.lostmekka.gamejam.teamsharks.helper.rect
import de.lostmekka.gamejam.teamsharks.sprite.Sprites
import de.lostmekka.gamejam.teamsharks.ui.GameplayUi
import de.lostmekka.gamejam.teamsharks.ui.loadSkin
import de.lostmekka.gamejam.teamsharks.util.GridPosition
import de.lostmekka.gamejam.teamsharks.util.GridSection
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.scene2d.Scene2DSkin
import ktx.actors.*

private val cellWidth get() = Gdx.graphics.width / (gridSize.x + 2f * borderSize.x)
private val cellHeight get() = Gdx.graphics.height / (gridSize.y + 2f * borderSize.y)

private class Cell(
    val rect: Rectangle,
    val pos: GridPosition,
)

private fun cell(x: Int, y: Int) =
    Cell(
        rect = Rectangle(
            x * cellWidth - cellWidth * gridSize.x / 2f,
            y * cellHeight - cellHeight * gridSize.y / 2f,
            cellWidth,
            cellHeight,
        ),
        pos = GridPosition(x, y),
    )

val GridSection.rect: Rectangle
    get() = Rectangle(
        x * cellWidth - cellWidth * gridSize.x / 2f,
        y * cellHeight - cellHeight * gridSize.y / 2f,
        w * cellWidth,
        h * cellHeight,
    )

private fun ResourceDeposit.rect(factoryDepth: Float): Rectangle {
    val x = grid.rect.x - borderSize.x * cellWidth / 2f
    val side = if (isLeft) 1 else -1
    val size = height
    val y = factoryDepth - depth + size / 2f - grid.h * cellHeight / 2f
    return Rectangle(x * side - size / 2f, y - size / 2f, size, size)
}

interface GameEventHandler {
    fun onMachineFinished(type: MachineType)
    fun onGameLost()
}

class GameplayScreen : KtxScreen, GameEventHandler {
    private val font = BitmapFont()
    private val spriteBatch = SpriteBatch().apply {
        color = Color.WHITE
    }
    private val shapeRenderer = ShapeRenderer().apply {
        color = Color.WHITE
        setAutoShapeType(true)
    }
    private val sprites = Sprites()
    private val sounds = Sounds()
    private val gameplayCamera = OrthographicCamera()
    private val gameplayViewport = ScreenViewport(gameplayCamera)
    private val stage: Stage = createStage()
    private val ui = GameplayUi()

    private val state = GameState()

    override fun show() {
        sounds.backgroundAtmo.play()
        sounds.backgroundAtmo.isLooping = true
    }

    override fun hide() {
        sounds.backgroundAtmo.stop()
    }

    override fun onMachineFinished(type: MachineType) {
        sounds.machineSounds[type]?.play()
        state.enemyAwareness += factoryBaseAwarenessMultiplier * state.factory.awarenessMultiplier
    }

    private fun onBribeClicked() {
        if (state.money < state.bribeCost) return
        state.money -= state.bribeCost
        state.enemyAwareness = 0f
        sounds.bribeSound.play()
    }

    private fun onSellClicked(it: ResourceAmount) {
        if (!state.sellResource(it)) return
        sounds.buySound.play()
    }

    private fun onUpgradeClicked(cell: Cell, blueprint: MachineBlueprint) {
        state.upgradeMachine(cell.pos, blueprint)
        sounds.buySound.play()
    }

    private fun onBuyClicked(cell: Cell, option: BuyOption) {
        state.buyMachine(cell.pos, option.blueprint)
        sounds.buySound.play()
    }

    override fun onGameLost() {
        sounds.backgroundAtmo.stop()
        sounds.gameOverSound.play()
    }

    override fun render(delta: Float) {
        ifKeyPressed(Input.Keys.ESCAPE) { Gdx.app.exit() }

        if (!state.gameLost) {
            // TODO: remove these debug cheat keys
            val delta = if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) delta * 50 else delta
            ifKeyPressed(Input.Keys.NUM_9) { state.factory.drillingSpeed = 100f }
            ifKeyPressed(Input.Keys.NUM_0) { state.factory.drillingSpeed = 5f }
            ifKeyPressed(Input.Keys.NUM_1) { state.factory += 100 * ResourceType.IronOre }
            ifKeyPressed(Input.Keys.NUM_2) { state.sellResource(1 * ResourceType.IronOre) }
            ifKeyPressed(Input.Keys.Q) {
                state.buyMachine(
                    GridPosition(0, 0),
                    machineBlueprints.getValue(MachineType.Smelter).first()
                )
            }
            ifKeyPressed(Input.Keys.W) {
                val pos = GridPosition(0, 0)
                val machine = state.factory[pos]
                machineBlueprints
                    .getValue(MachineType.Smelter)
                    .getOrNull(machine?.tier ?: -1)
                    ?.also { state.upgradeMachine(pos, it) }
            }

            state.update(delta, this)
            stage.act(delta)
        }

        spriteBatch.use(gameplayCamera) {
            val dirtSize = dirtLayerScale.toFloat()
            // Draw background Earth
            for (x in 0..(Gdx.graphics.width / dirtLayerScale)) {
                for (y in 0..(Gdx.graphics.height / dirtLayerScale + 1)) {
                    it.color = Color.WHITE // TODO: use noise
                    it.draw(
                        sprites.backgroundEarth,
                        x * dirtSize - Gdx.graphics.width / 2f,
                        (y - 1) * dirtSize + state.dirtLayerOffset - Gdx.graphics.height / 2f,
                        dirtSize,
                        dirtSize,
                    )
                }
            }
            // Draw resource deposits
            it.color = Color.WHITE
            for (deposit in state.currentResourceDeposits) {
                val rect = deposit.rect(state.factory.depth)
                it.color = if (deposit.isMined) Color.WHITE else Color.RED
                it.draw(sprites.resourceDeposits.getValue(deposit.resourceType), rect)
            }
            // Draw Mined background
            it.color = Color.WHITE
            it.draw(
                sprites.backgroundMined,
                grid.rect.getX() - 100f,
                grid.rect.getY() + 500f,
                sprites.backgroundMined.width.toFloat(),
                sprites.backgroundMined.height.toFloat(),
            )
            // Draw Factory
            it.color = Color.WHITE
            it.draw(
                sprites.factoryAnimation.getKeyFrame(delta),
                grid.rect.getX() - 100f,
                grid.rect.getY() - 60f,
                factoryWidth.toFloat(),
                factoryHeight.toFloat(),
            )
            // Draw machines
            for (x in 0 until gridSize.x) {
                for (y in 0 until gridSize.y) {
                    val cell = cell(x, y)
                    val machine = state.factory[cell.pos] ?: continue
                    it.draw(
                        sprites.machinesSprites[machine.machineType] ?: sprites.machineErrorSprite,
                        cell.rect.getX(),
                        cell.rect.getY(),
                        cell.rect.height,
                        cell.rect.height,
                    )
                }
            }
            // Draw particles
            // TODO: find out why this causes performance issues
//            it.color = Color.WHITE
//            sprites.dirtParticles.forEach { effect ->
//                effect.draw(it, delta)
//                if (effect.isComplete)
//                    effect.free()
//            }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Line, gameplayCamera) {
            it.color = Color.WHITE
            for (x in 0 until gridSize.x) {
                for (y in 0 until gridSize.y) {
                    val cell = cell(x, y)
                    if (cell.pos !in inventorySpace) it.rect(cell.rect)
                }
            }
            it.rect(inventorySpace.rect)
        }

        runGuiCleanup()

        val buyOptions = machineBlueprints.map { (type, blueprints) ->
            val blueprint = blueprints.first()
            BuyOption(
                name = blueprint.name,
                machineType = type,
                cost = blueprint.cost,
                canAfford = state.money >= blueprint.cost,
                blueprint = blueprint,
            )
        }
        for (x in 0 until gridSize.x) {
            for (y in 0 until gridSize.y) {
                val cell = cell(x, y)
                if (cell.pos in inventorySpace) continue
                val machine = state.factory[cell.pos]
                if (machine == null) {
                    renderEmptyCell(
                        pos = cell.pos,
                        rect = cell.rect,
                        buyOptions = buyOptions,
                        onBuyClicked = { onBuyClicked(cell, it) }
                    )
                } else {
                    val nextTier = machineBlueprints[machine.machineType]?.getOrNull(machine.tier)
                    renderMachineCell(
                        pos = cell.pos,
                        rect = cell.rect,
                        machine = MachineStatus(
                            name = machine.name,
                            tier = machine.tier,
                            workProgress = machine.progress,
                            isWorking = machine.isWorking,
                            upgradeAvailable = nextTier != null,
                            upgradeAffordable = nextTier != null && state.money >= nextTier.cost,
                            upgradeCost = nextTier?.cost ?: 0,
                        ),
                        onUpgradeClicked = { nextTier?.let { onUpgradeClicked(cell, it) } }
                    )
                }
            }
        }
        val inventoryContent = ResourceType.values().map { state.factory[it] * it }
        renderInventory(inventorySpace.rect, inventoryContent) { onSellClicked(it) }
        for (depot in state.currentResourceDeposits) {
            renderResourceDepositGui(depot.rect(state.factory.depth), depot.resourceType, depot.resourceAmount)
        }
        // TODO: better logic for bribe cost and effect
        renderStaticGui(
            currentDepth = state.factory.depth,
            money = state.money,
            awareness = state.enemyAwareness,
            bribeCost = state.bribeCost,
            onBribeClicked = { this.onBribeClicked() },
        )
        stage.draw()
    }

    data class BuyOption(
        val name: String,
        val machineType: MachineType,
        val cost: Int,
        val canAfford: Boolean,
        val blueprint: MachineBlueprint,
    )

    fun renderEmptyCell(
        pos: GridPosition,
        rect: Rectangle,
        buyOptions: List<BuyOption>,
        onBuyClicked: (BuyOption) -> Unit,
    ) {
        ui.renderEmptyCell(stage, pos, convertToUiCoords(rect, true), buyOptions, onBuyClicked)
    }

    data class MachineStatus(
        val name: String,
        val tier: Int,
        val workProgress: Float,
        val isWorking: Boolean,
        val upgradeAvailable: Boolean,
        val upgradeAffordable: Boolean,
        val upgradeCost: Int
    )

    fun renderMachineCell(
        pos: GridPosition,
        rect: Rectangle,
        machine: MachineStatus,
        onUpgradeClicked: () -> Unit,
    ) {
        ui.renderMachineCell(
            stage,
            pos,
            convertToUiCoords(rect, true),
            machine,
            onUpgradeClicked,
        )
    }

    fun renderInventory(
        rect: Rectangle,
        content: List<ResourceAmount>,
        onSellClicked: (ResourceAmount) -> Unit,
    ) {
        ui.renderInventory(
            stage,
            convertToUiCoords(rect),
            content,
            onSellClicked,
            sprites
        )
    }

    fun renderStaticGui(
        currentDepth: Float,
        money: Int,
        awareness: Float,
        bribeCost: Int,
        onBribeClicked: () -> Unit,
    ) {
        ui.renderStaticUi(
            stage,
            sprites,
            currentDepth,
            money,
            awareness,
            bribeCost,
            onBribeClicked,
        )
        ui.renderHelpMenu(stage)
    }

    fun runGuiCleanup() {
        ui.cleanupResourceDeposits(stage)
    }

    fun renderResourceDepositGui(
        rect: Rectangle,
        resourceType: ResourceType,
        resourceAmount: Int,
    ) {
        ui.renderResourceDeposit(
            stage,
            sprites,
            convertToUiCoords(rect, true),
            resourceType,
            resourceAmount,
        )
    }

    private fun createStage(): Stage {
        val stage = stage(viewport = ScreenViewport(), batch = spriteBatch)
        Gdx.input.inputProcessor = stage
        Scene2DSkin.defaultSkin = loadSkin(sprites)

        return stage
    }

    override fun resize(width: Int, height: Int) {
        gameplayViewport.update(width, height)
        stage.viewport.update(width, height)
    }

    fun convertToUiCoords(rect: Rectangle, flipV: Boolean = false): Rectangle {
        return stage.viewport.unproject(gameplayViewport.project(Vector2(rect.x, rect.y))).let {
            if (flipV)
                Rectangle(it.x, Gdx.graphics.height - it.y, rect.width, rect.height)
            else
                Rectangle(it.x, it.y, rect.width, rect.height)
        }
    }

    override fun dispose() {
        font.disposeSafely()
        spriteBatch.disposeSafely()
        sprites.disposeSafely()
        stage.disposeSafely()
    }
}
