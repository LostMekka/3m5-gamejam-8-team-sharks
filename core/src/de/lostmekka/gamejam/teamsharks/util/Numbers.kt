package de.lostmekka.gamejam.teamsharks.util


class Progress(
    var updateTime: Float,
) {
    private val max = 101
    private var current = 0
    private var passedTime = 0f

    fun increment(value: Float): Int {
        passedTime += value
        if (passedTime >= updateTime) {
            passedTime = 0f
            current = (++current % max)
        }
        return current
    }

    fun decrement(value: Float): Int {
        passedTime -= value

        if (passedTime <= 0f)
            passedTime = 0f

        if (current - 1 >= 0)
            --current

        return current
    }

    fun isMax() = current == max - 1

    fun reset() {
        current = 0
        passedTime = 0f
    }
}