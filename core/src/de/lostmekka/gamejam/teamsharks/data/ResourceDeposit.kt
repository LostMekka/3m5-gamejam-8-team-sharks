package de.lostmekka.gamejam.teamsharks.data

import kotlin.random.Random

class ResourceDeposit(
    val depth: Float,
    val height: Float,
    val resourceType: ResourceType,
    var resourceAmount: Int,
    val isLeft: Boolean = Random.nextBoolean(),
) {
    val coveringHeightRange = (depth - height)..depth
    var isMined = false

    private var timer = 0f

    fun update(
        deltaTime: Float,
        miningSpeed: Float,
        factoryHeightRange: ClosedFloatingPointRange<Float>,
    ): ResourceAmount {
        isMined = resourceAmount > 0 && this touches factoryHeightRange
        if (!isMined) return 0 * resourceType

        timer += deltaTime * miningSpeed
        val amount = minOf(timer.toInt(), resourceAmount)
        resourceAmount -= amount
        timer %= 1f
        return amount * resourceType
    }

    infix fun touches(heightRange: ClosedFloatingPointRange<Float>) =
        coveringHeightRange.start in heightRange ||
            coveringHeightRange.endInclusive in heightRange ||
            heightRange.start in coveringHeightRange ||
            heightRange.endInclusive in coveringHeightRange
}
