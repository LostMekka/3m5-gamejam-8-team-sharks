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
import com.kotcrab.vis.ui.VisUI
import de.lostmekka.gamejam.teamsharks.data.GameConstants.borderSize
import de.lostmekka.gamejam.teamsharks.data.GameConstants.dirtLayerScale
import de.lostmekka.gamejam.teamsharks.data.GameConstants.grid
import de.lostmekka.gamejam.teamsharks.data.GameConstants.gridSize
import de.lostmekka.gamejam.teamsharks.data.GameConstants.inventorySpace
import de.lostmekka.gamejam.teamsharks.data.GameState
import de.lostmekka.gamejam.teamsharks.data.MachineBlueprint
import de.lostmekka.gamejam.teamsharks.data.MachineType
import de.lostmekka.gamejam.teamsharks.data.ResourceAmount
import de.lostmekka.gamejam.teamsharks.data.times
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import de.lostmekka.gamejam.teamsharks.data.machineBlueprints
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

private val GridSection.rect: Rectangle
    get() = Rectangle(
        x * cellWidth - cellWidth * gridSize.x / 2f,
        y * cellHeight - cellHeight * gridSize.y / 2f,
        w * cellWidth,
        h * cellHeight,
    )

class GameplayScreen : KtxScreen {
    private val font = BitmapFont()
    private val spriteBatch = SpriteBatch().apply {
        color = Color.WHITE
    }
    private val shapeRenderer = ShapeRenderer().apply {
        color = Color.WHITE
        setAutoShapeType(true)
    }
    private val sprites = Sprites()
    private val gameplayCamera = OrthographicCamera()
    private val gameplayViewport = ScreenViewport(gameplayCamera)
    private val stage: Stage = createStage()
    private val ui = GameplayUi()

    private val state = GameState()

    override fun render(delta: Float) {
        ifKeyPressed(Input.Keys.ESCAPE) { Gdx.app.exit() }

        // TODO: remove these debug cheat keys
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

        state.update(delta)
        stage.act(delta)

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
            // Draw Factory
            it.draw(
                sprites.backgroundFactory,
                grid.rect.getX() - 100f,
                grid.rect.getY() - 60f,
                sprites.backgroundFactory.width.toFloat(),
                sprites.backgroundFactory.height.toFloat(),
            )
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.BLACK
            it.rect(grid.rect)
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

        spriteBatch.use(gameplayCamera) {
            for ((i, resourceType) in ResourceType.values().withIndex()) {
                it.draw(sprites.resourceIcons[resourceType], 10f, 50f + 40f * i)
            }
        }

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
                        rect = cell.rect,
                        buyOptions = buyOptions,
                        onBuyClicked = { state.buyMachine(cell.pos, it.blueprint) }
                    )
                } else {
                    val nextTier = machineBlueprints[machine.machineType]?.getOrNull(machine.tier)
                    renderMachineCell(
                        rect = cell.rect,
                        machine = MachineStatus(
                            name = machine.name,
                            tier = machine.tier,
                            workProgress = machine.progress,
                            upgradeAvailable = nextTier != null,
                            upgradeAffordable = nextTier != null && state.money >= nextTier.cost,
                            upgradeCost = nextTier?.cost ?: 0,
                        ),
                        onUpgradeClicked = { nextTier?.let { state.upgradeMachine(cell.pos, it) } }
                    )
                }
            }
        }
        val inventoryContent = ResourceType.values().map { state.factory[it] * it }
        renderInventory(inventorySpace.rect, inventoryContent) { state.sellResource(it) }
        // TODO: better logic for bribe cost and effect
        renderStaticGui(
            currentDepth = state.factory.depth,
            money = state.money,
            awareness = state.enemyAwareness,
            bribeCost = 10,
            onBribeClicked = { state.enemyAwareness *= 0.5f }
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
        rect: Rectangle,
        buyOptions: List<BuyOption>,
        onBuyClicked: (BuyOption) -> Unit,
    ) {
        // TODO: implement
    }

    data class MachineStatus(
        val name: String,
        val tier: Int,
        val workProgress: Float,
        val upgradeAvailable: Boolean,
        val upgradeAffordable: Boolean,
        val upgradeCost: Int,
    )

    fun renderMachineCell(
        rect: Rectangle,
        machine: MachineStatus,
        onUpgradeClicked: () -> Unit,
    ) {
        // TODO: implement
    }

    fun renderInventory(
        rect: Rectangle,
        content: List<ResourceAmount>,
        onSellClicked: (ResourceAmount) -> Unit,
    ) {
        ui.renderInventory(
            stage,
            stage.viewport.unproject(gameplayViewport.project(Vector2(rect.x, rect.y))).let {
                Rectangle(it.x, it.y, rect.width, rect.height)
            },
            content,
            onSellClicked
        )
    }

    fun renderStaticGui(
        currentDepth: Float,
        money: Int,
        awareness: Float,
        bribeCost: Int,
        onBribeClicked: () -> Unit,
    ) {
        ui.renderStaticUi(stage, state)
    }

    private fun createStage(): Stage {
        val stage = stage(viewport = ScreenViewport(), batch = spriteBatch)
        stage.setDebugUnderMouse(true)
        Gdx.input.inputProcessor = stage
        Scene2DSkin.defaultSkin = loadSkin()

        return stage
    }

    override fun resize(width: Int, height: Int) {
        gameplayViewport.update(width, height)
        stage.viewport.update(width, height)
    }

    override fun dispose() {
        font.disposeSafely()
        spriteBatch.disposeSafely()
        sprites.disposeSafely()
        stage.disposeSafely()
    }
}
