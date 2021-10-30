package de.lostmekka.gamejam.teamsharks

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import de.lostmekka.gamejam.teamsharks.sprite.Sprites
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class GameplayScreen : KtxScreen {
    private val font = BitmapFont()
    private val spriteBatch = SpriteBatch().apply {
        color = Color.WHITE
    }
    private val sprites = Sprites()

    override fun render(delta: Float) {
        spriteBatch.use {
            font.draw(it, "Hello Kotlin!", 100f, 100f)
            for ((i, resourceType) in ResourceType.values().withIndex()) {
                it.draw(sprites.resourceIcons[resourceType], 10f, 50f + 40f * i)
            }
        }
    }

    override fun dispose() {
        font.disposeSafely()
        spriteBatch.disposeSafely()
        sprites.disposeSafely()
    }
}
