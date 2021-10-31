package de.lostmekka.gamejam.teamsharks.helper

import com.badlogic.gdx.Gdx

fun ifKeyPressed(keyCode: Int, block: () -> Unit) {
    if (Gdx.input.isKeyJustPressed(keyCode)) block()
}
