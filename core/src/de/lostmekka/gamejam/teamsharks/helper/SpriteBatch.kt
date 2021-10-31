package de.lostmekka.gamejam.teamsharks.helper

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

fun SpriteBatch.draw(tex: Texture, rect: Rectangle) {
    draw(tex, rect.x, rect.y, rect.width, rect.height)
}

fun SpriteBatch.draw(tex: TextureRegion, rect: Rectangle) {
    draw(tex, rect.x, rect.y, rect.width, rect.height)
}
