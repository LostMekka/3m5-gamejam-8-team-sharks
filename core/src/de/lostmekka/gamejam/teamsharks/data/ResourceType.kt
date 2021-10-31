package de.lostmekka.gamejam.teamsharks.data

enum class ResourceType {
    IronOre,
    CopperOre,
    IronIngot,
    CopperIngot,
    CopperWire,
    Coal,
    Oil,
    SteelBeam,;
    //...

    override fun toString(): String =
        when (this) {
            IronOre -> "Iron Ore"
            CopperOre -> "Copper Ore"
            IronIngot -> "Iron Ingot"
            CopperIngot -> "Copper Ingot"
            CopperWire -> "Copper Wire"
            Coal -> "Coal"
            Oil -> "Oil"
            SteelBeam -> "Steel Beam"
        }
}

data class ResourceAmount(
    val resourceType: ResourceType,
    val amount: Int,
)

operator fun Int.times(resourceType: ResourceType) = ResourceAmount(resourceType, this)
