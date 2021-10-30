package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridPosition

class SoilLayer(
    val resources: List<ResourceType>,
    val position: GridPosition,
) {
    private val LAYER_SIZE = 5 // TODO
    private val FACTORY_HEIGHT = 10 // TODO

    fun isColliding(yCoordinate: Int) =
        position.y >= yCoordinate && position.y + LAYER_SIZE <= yCoordinate + FACTORY_HEIGHT
}