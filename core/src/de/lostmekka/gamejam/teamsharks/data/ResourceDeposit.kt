package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.Timer
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

    private val timer = Timer(1f)

    fun update(
        deltaTime: Float,
        miningSpeed: Float,
        factoryHeightRange: ClosedFloatingPointRange<Float>,
    ): ResourceAmount {
        isMined = resourceAmount > 0 && this touches factoryHeightRange
        if (!isMined) return 0 * resourceType

        val amount = minOf(
            timer.increment(deltaTime * miningSpeed),
            resourceAmount,
        )
        resourceAmount -= amount
        return amount * resourceType
    }

    infix fun touches(heightRange: ClosedFloatingPointRange<Float>) =
        coveringHeightRange.start in heightRange ||
            coveringHeightRange.endInclusive in heightRange ||
            heightRange.start in coveringHeightRange ||
            heightRange.endInclusive in coveringHeightRange
}
