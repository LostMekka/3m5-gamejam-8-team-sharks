package de.lostmekka.gamejam.teamsharks

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import de.lostmekka.gamejam.teamsharks.data.MachineType
import ktx.assets.disposeSafely

class Sounds {
    private val allSounds = mutableListOf<Sound>()
    private val allMusics = mutableListOf<Music>()

    val buySound = loadSound("audio/money.mp3")
    val bribeSound = loadSound("audio/bribe.mp3")
    val gameOverSound = loadSound("audio/youlost.mp3")
    val backgroundAtmo = loadMusic("audio/background/machine_atmo-loop.mp3", 1.45)
    val machineSounds = mapOf(
        MachineType.Smelter to loadSound("audio/machines/steam2.mp3"),
        MachineType.Furnace to loadSound("audio/machines/steam.mp3"),
        MachineType.WireMaker to loadSound("audio/machines/hammer-impact.mp3"),
        MachineType.ChemLab to loadSound("audio/machines/bubbling.mp3"),
    )

    fun disposeSafely() {
        allSounds.disposeSafely()
        allMusics.disposeSafely()
    }

    private fun loadSound(path: String, volume: Number = 1): Sound =
        Gdx.audio.newSound(Gdx.files.internal(path))
            .let { SoundWithDefaultVolume(it, volume.toFloat()) }
            .also { allSounds += it }

    private fun loadMusic(path: String, volume: Number = 1): Music =
        Gdx.audio.newMusic(Gdx.files.internal(path)).also {
            allMusics += it
            it.volume = volume.toFloat()
        }
}

class SoundWithDefaultVolume(inner: Sound, val defaultVolume: Float = 1f) : Sound by inner {
    override fun play(): Long = play(defaultVolume)
}
