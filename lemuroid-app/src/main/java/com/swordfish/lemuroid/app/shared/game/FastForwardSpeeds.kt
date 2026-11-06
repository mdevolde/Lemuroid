package com.swordfish.lemuroid.app.shared.game

internal object FastForwardSpeeds {
    const val PREFERRED_SPEED_KEY = "fast_forward_preferred_speed"

    val supported = listOf(1, 2, 4, 8, 16)

    fun validate(speed: Int): Int = speed.takeIf { it in supported } ?: 1

    fun preferred(speed: Int): Int = validate(speed).takeIf { it > 1 } ?: 2

    fun toggle(
        current: Int,
        selected: Int,
    ): Int = if (current > 1) 1 else preferred(selected)

    fun select(
        current: Int,
        selected: Int,
    ): Int = if (current > 1) preferred(selected) else 1

    fun cycle(raw: Set<String>?): List<Int> {
        val speeds =
            (raw ?: supported.map { it.toString() }.toSet())
                .mapNotNull { it.toIntOrNull() }
                .filter { it in supported }
                .toMutableSet()
        speeds.add(1)
        if (speeds.size == 1) speeds.add(2)
        return speeds.sorted()
    }

    fun next(
        current: Int,
        cycle: List<Int>,
    ): Int {
        val index = cycle.indexOf(current)
        return if (index < 0) 1 else cycle[(index + 1) % cycle.size]
    }
}
