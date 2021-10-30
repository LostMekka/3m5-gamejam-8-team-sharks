package de.lostmekka.gamejam.teamsharks

import com.badlogic.gdx.Screen
import ktx.app.KtxGame

class GameJam8Game : KtxGame<Screen>() {
    override fun create() {
        addScreen(GameplayScreen())
        setScreen<GameplayScreen>()
    }
}
