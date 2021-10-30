package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.Timer

class ResourceDeposit(
    val depth: Float,
    val height: Float,
    val resourceType: ResourceType,
    var resourceAmount: Int,
) {
    val coveringHeightRange = (depth - height)..depth

    private val timer = Timer(1f)

    fun update(deltaTime: Float, miningSpeed: Float): ResourceAmount {
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
