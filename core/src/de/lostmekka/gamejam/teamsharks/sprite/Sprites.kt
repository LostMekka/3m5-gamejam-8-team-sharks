package de.lostmekka.gamejam.teamsharks.sprite

import com.badlogic.gdx.graphics.Texture
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import ktx.assets.disposeSafely

class Sprites {
    val resourceIcons = ResourceType.values().associateWith { loadTexture("sprites/resources/$it.png") }

    fun disposeSafely() {
        resourceIcons.values.disposeSafely()
    }
}

private fun loadTexture(path: String): Texture =
    Texture(path).apply {
        setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
    }
