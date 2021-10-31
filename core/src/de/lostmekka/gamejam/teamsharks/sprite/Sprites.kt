package de.lostmekka.gamejam.teamsharks.sprite

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import ktx.assets.disposeSafely

class Sprites {
    private val allSprites = mutableListOf<Texture>()

    val resourceIcons = ResourceType.values().associateWith { loadTexture("sprites/resources/$it.png") }
    val backgroundEarth = loadTexture("sprites/background/BgEarth.png")
    val backgroundFactory = loadTexture("sprites/background/Factory.png")
    val resourceDeposits = loadTexture("sprites/resources/Ores.png")
        .split(96, 96, 5, 1)
        .let {
            mapOf(
                ResourceType.IronOre to it[3],
                ResourceType.CopperOre to it[1],
            )
        }

    fun disposeSafely() {
        allSprites.disposeSafely()
    }

    private fun loadTexture(path: String): Texture =
        Texture(path).also {
            it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            allSprites += it
        }
}

private fun Texture.split(
    spriteWidth: Int,
    spriteHeight: Int,
    atlasWidth: Int,
    atlasHeight: Int,
): MutableList<TextureRegion> {
    val sprites = mutableListOf<TextureRegion>()
    for (y in 0 until atlasHeight) {
        for (x in 0 until atlasWidth) {
            sprites += TextureRegion(this, x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight)
        }
    }
    return sprites
}
