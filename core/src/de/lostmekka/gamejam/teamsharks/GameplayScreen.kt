package de.lostmekka.gamejam.teamsharks

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.lostmekka.gamejam.teamsharks.data.GameConstants.borderSize
import de.lostmekka.gamejam.teamsharks.data.GameConstants.gridSize
import de.lostmekka.gamejam.teamsharks.data.GameConstants.inventorySpace
import de.lostmekka.gamejam.teamsharks.data.GameState
import de.lostmekka.gamejam.teamsharks.data.ItemType
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import de.lostmekka.gamejam.teamsharks.helper.ifKeyPressed
import de.lostmekka.gamejam.teamsharks.helper.rect
import de.lostmekka.gamejam.teamsharks.sprite.Sprites
import de.lostmekka.gamejam.teamsharks.util.GridPosition
import de.lostmekka.gamejam.teamsharks.util.GridSection
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

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

    private val state = GameState()

    override fun render(delta: Float) {
        ifKeyPressed(Input.Keys.ESCAPE) { Gdx.app.exit() }

        ifKeyPressed(Input.Keys.A) { state.sell(ResourceType.IronOre, 1) }

        state.update(delta)

        shapeRenderer.use(ShapeRenderer.ShapeType.Line, gameplayCamera) {
            for (x in 0 until gridSize.x) {
                for (y in 0 until gridSize.y) {
                    val cell = cell(x, y)
                    if (cell.pos !in inventorySpace) it.rect(cell.rect)
                }
            }
            it.rect(inventorySpace.rect)
        }

        spriteBatch.use(gameplayCamera) {
            font.draw(it, "Hello Kotlin!", 100f, 100f)
            for ((i, resourceType) in ResourceType.values().withIndex()) {
                it.draw(sprites.resourceIcons[resourceType], 10f, 50f + 40f * i)
            }
        }
    }

    fun checkButtons() {
        // TODO: Add triggers
        // sell item/s
        state.sell(ResourceType.IronOre, 1)
        // upgrade/buy machine
        state.upgrade(GridPosition(0, 0))
    }

    override fun resize(width: Int, height: Int) {
        gameplayViewport.update(width, height)
    }

    override fun dispose() {
        font.disposeSafely()
        spriteBatch.disposeSafely()
        sprites.disposeSafely()
    }
}
