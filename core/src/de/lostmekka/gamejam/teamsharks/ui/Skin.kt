package de.lostmekka.gamejam.teamsharks.ui

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.kotcrab.vis.ui.VisUI
import ktx.style.*

fun loadSkin(): Skin {
    VisUI.load()
    val skin = VisUI.getSkin()

    return skin.apply {
        progressBar(extend = "default-horizontal") {
            knob.minHeight = 40f
            background.minHeight = 40f
        }

        button(name = "bribe") {
        }
    }
}
