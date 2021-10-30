package de.lostmekka.gamejam.teamsharks.sprite

import com.badlogic.gdx.graphics.Texture
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import ktx.assets.disposeSafely

class Sprites {
    private val allSprites = mutableListOf<Texture>()

    val resourceIcons = ResourceType.values().associateWith { loadTexture("sprites/resources/$it.png") }
    val backgroundEarth = loadTexture("sprites/background/BgEarth.png")

    fun disposeSafely() {
        allSprites.disposeSafely()
    }

    private fun loadTexture(path: String): Texture =
        Texture(path).also {
            it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            allSprites += it
        }
}
