package com.swordfish.lemuroid.app.shared.game

internal data class FastForwardState(
    val enabled: Boolean = false,
    val suspended: Boolean = false,
    val preferredSpeed: Int = 2,
) {
    val frameSpeed: Int get() = if (enabled && !suspended) preferredSpeed else 1

    fun setEnabled(value: Boolean): FastForwardState = copy(enabled = value, suspended = false)

    fun toggleOsd(): FastForwardState = if (enabled) copy(suspended = !suspended) else this

    fun selectSpeed(speed: Int): FastForwardState = copy(preferredSpeed = FastForwardSpeeds.preferred(speed))

    fun setFrameSpeed(speed: Int): FastForwardState {
        val validated = FastForwardSpeeds.validate(speed)
        return copy(
            enabled = validated > 1,
            suspended = false,
            preferredSpeed = if (validated > 1) validated else preferredSpeed,
        )
    }
}
