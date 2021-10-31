package de.lostmekka.gamejam.teamsharks.ui

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.kotcrab.vis.ui.VisUI
import de.lostmekka.gamejam.teamsharks.sprite.Sprites
import ktx.style.*

fun loadSkin(sprites: Sprites): Skin {
    VisUI.load()
    val skin = VisUI.getSkin()

    return skin.apply {
        progressBar(name = "default-horizontal", extend = "default-horizontal") {
            sprites.uiSprites["progressFillHorizontal"]?.let {
                knobBefore = TextureRegionDrawable(it).also { it.minHeight = 40f }
                knob = TextureRegionDrawable(it).also { it.minHeight = 40f }
            }

            sprites.uiSprites["progressFillHorizontalAttention"]?.let {
                disabledKnobBefore = TextureRegionDrawable(it).also { it.minHeight = 40f }
                disabledKnob = TextureRegionDrawable(it).also { it.minHeight = 40f }
            }

            background.minHeight = 40f
        }

        progressBar(name = "default-vertical", extend = "default-vertical") {
            sprites.uiSprites["progressFillVertical"]?.let {
                knobBefore = TextureRegionDrawable(it).also { it.minWidth = 10f }
                knob = TextureRegionDrawable(it).also { it.minWidth = 10f }
            }

            background.minWidth = 10f
        }

        button(name = "bribe") {
        }
    }
}
