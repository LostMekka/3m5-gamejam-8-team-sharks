package de.lostmekka.gamejam.teamsharks.data

class Recipe(
    val consumedResources: List<ResourceAmount>,
    val producedResources: List<ResourceAmount>,
    var baseDuration: Float,
)
