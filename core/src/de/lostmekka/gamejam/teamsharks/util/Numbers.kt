package de.lostmekka.gamejam.teamsharks.util


class Timer(
    var updateTime: Float,
) {
    private var passedTime = 0f

    val progress = passedTime / updateTime

    fun increment(value: Float): Int {
        passedTime += value
        var finished = 0
        while (passedTime >= updateTime) {
            passedTime -= updateTime
            finished++
        }
        return finished
    }

    fun decrement(value: Float): Int {
        passedTime -= value
        var finished = 0
        while (passedTime < 0f) {
            passedTime -= updateTime
            finished++
        }
        return finished
    }

    fun reset() {
        passedTime = 0f
    }
}
