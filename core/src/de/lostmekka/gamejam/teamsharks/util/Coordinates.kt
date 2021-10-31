package de.lostmekka.gamejam.teamsharks.util

data class GridPosition(val x: Int, val y: Int) {
    override fun hashCode(): Int = x * 100 + y
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GridPosition

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
}

data class GridSize(val x: Int, val y: Int)
infix fun Int.by(y: Int) = GridSize(this, y)

data class GridSection(val x: Int, val y: Int, val w: Int, val h: Int) {
    constructor(x: Int, y: Int, size: GridSize): this(x, y, size.x, size.y)
    operator fun contains(pos: GridPosition) = contains(pos.x, pos.y)
    fun contains(x: Int, y: Int) = x - this.x in 0 until w && y - this.y in 0 until h
}
