package de.lostmekka.gamejam.teamsharks.data

import de.lostmekka.gamejam.teamsharks.util.GridSection
import de.lostmekka.gamejam.teamsharks.util.by

object GameConstants {
    val gridSize = 10 by 10
    val borderSize = 2 by 2
    val grid = GridSection(0, 0, gridSize)
    val inventorySpace = GridSection(0, 5, 3 by 5)
}
