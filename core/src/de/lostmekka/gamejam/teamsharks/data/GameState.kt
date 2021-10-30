package de.lostmekka.gamejam.teamsharks.data

class GameState {
    val inventory = mutableMapOf<ResourceType, Int>().withDefault { 0 }
    var money = 0
    var enemyAwareness = 0f
}

data class GridPosition(val x: Int, val y: Int)

class Machine(
    val name: String,
    val position: GridPosition,
    //...
)
