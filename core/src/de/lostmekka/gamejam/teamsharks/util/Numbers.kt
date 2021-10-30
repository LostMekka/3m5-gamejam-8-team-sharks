package de.lostmekka.gamejam.teamsharks.util

class Progress {
    private val max: Int = 101
    private var current: Int = 0

    fun increment(value: Int = 1): Int =
        current.apply { value.plus(current) % max }

    fun decrement(value: Int = 1): Int =
        current.apply { (current - value) % max }

    fun isMax() = current == max - 1

    fun reset() { current = 0 }
}