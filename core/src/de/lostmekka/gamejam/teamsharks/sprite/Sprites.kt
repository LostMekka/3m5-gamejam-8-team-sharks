package de.lostmekka.gamejam.teamsharks.sprite

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import de.lostmekka.gamejam.teamsharks.data.GameConstants.factoryHeight
import de.lostmekka.gamejam.teamsharks.data.GameConstants.factoryWidth
import de.lostmekka.gamejam.teamsharks.data.GameConstants.machineScale
import de.lostmekka.gamejam.teamsharks.data.MachineType
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import ktx.assets.disposeSafely
import ktx.collections.toGdxArray

class Sprites {
    private val allSprites = mutableListOf<Texture>()

    val resourceIcons = ResourceType.values().associateWith { loadTexture("sprites/resources/$it.png") }
    val icons = loadTexture("sprites/Icons.png")
        .split(27, 27, 50, 1)
    val backgroundEarth = loadTexture("sprites/background/BgEarth.png")
    val factoryAnimation = Animation(
        0.05f,
        loadTexture("sprites/background/Factory.png")
            .split(factoryWidth, factoryHeight, 1, 2)
            .toGdxArray(true),
    ).also { it.playMode = Animation.PlayMode.LOOP_PINGPONG }
    val backgroundMined = loadTexture("sprites/background/BgMined.png")
    val resourceDeposits = loadTexture("sprites/resources/Ores.png")
        .split(96, 96, 5, 1)
        .let {
            mapOf(
                ResourceType.IronOre to it[3],
                ResourceType.CopperOre to it[1],
                ResourceType.Coal to it[0],
            )
        }
    val machinesSprites = loadTexture("sprites/background/Machines.png")
        .split(machineScale, machineScale, 5, 5)
        .let {
            mapOf(
                MachineType.OreCrusher to it[0],
                MachineType.DrillModule to it[1],
                MachineType.MiningModule to it[2],
                MachineType.Smelter to it[3],
                MachineType.Furnace to it[3], // TODO: create separate sprite
                MachineType.WireMaker to it[5],
                MachineType.ChemLab to it[5], // TODO: create separate sprite
            )
        }
    val machineErrorSprite = loadTexture("sprites/background/Machines.png")
        .split(machineScale, machineScale, 5, 5)[24]

    val dirtParticlesPool = ParticleEffectPool(
        ParticleEffect()
            .also {
                it.load(
                    Gdx.files.internal("sprites/background/EarthParticles.p"),
                    TextureAtlas(Gdx.files.internal("sprites/background/atlas/Earth.atlas"))
                )
                it.setEmittersCleanUpBlendFunction(true)
                it.start()
            },
        1,
        2
    )
    val dirtParticles =
        (0..20).map { x ->
            dirtParticlesPool.obtain().also { it.setPosition(-750f + (x * 73), -420f) }
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
