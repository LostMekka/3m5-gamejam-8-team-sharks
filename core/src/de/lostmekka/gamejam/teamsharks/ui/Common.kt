package de.lostmekka.gamejam.teamsharks.ui

import com.badlogic.gdx.graphics.Color
import com.kotcrab.vis.ui.widget.VisLabel
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visLabel

fun createFixedWidthLabel(text: CharSequence) : VisLabel {
    return scene2d.visLabel(text) {
        this.width = 200f
    }
}

