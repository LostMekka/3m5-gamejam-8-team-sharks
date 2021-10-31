package de.lostmekka.gamejam.teamsharks.data

enum class ResourceType {
    IronOre,
    CopperOre,
    IronIngot,
    CopperIngot,
    Coal,
    SteelBeam,
    //...
}

data class ResourceAmount(
    val resourceType: ResourceType,
    val amount: Int,
)

operator fun Int.times(resourceType: ResourceType) = ResourceAmount(resourceType, this)
