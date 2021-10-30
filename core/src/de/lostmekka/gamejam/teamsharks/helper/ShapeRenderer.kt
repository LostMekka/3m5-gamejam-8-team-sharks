package de.lostmekka.gamejam.teamsharks.helper

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle

fun ShapeRenderer.rect(rect: Rectangle) {
    rect(rect.x, rect.y, rect.width, rect.height)
}
