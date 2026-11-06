package com.swordfish.lemuroid.app.shared.game

import org.junit.Assert.assertEquals
import org.junit.Test

class FastForwardSpeedsTest {
    @Test
    fun defaultCycleIncludesEverySupportedSpeed() {
        assertEquals(listOf(1, 2, 4, 8, 16), FastForwardSpeeds.cycle(null))
    }

    @Test
    fun cancelledEmptyOrNormalOnlySelectionsStillAllowFastForward() {
        assertEquals(listOf(1, 2), FastForwardSpeeds.cycle(emptySet()))
        assertEquals(listOf(1, 2), FastForwardSpeeds.cycle(setOf("1")))
    }

    @Test
    fun customCycleAlwaysIncludesNormalSpeedAndIsSorted() {
        assertEquals(listOf(1, 4, 16), FastForwardSpeeds.cycle(setOf("16", "4")))
    }

    @Test
    fun corruptedPreferencesCannotSendUnsupportedSpeedsToTheCore() {
        assertEquals(listOf(1, 8), FastForwardSpeeds.cycle(setOf("8", "0", "-1", "3", "999", "bad")))
        assertEquals(listOf(1, 2), FastForwardSpeeds.cycle(setOf("bad", "0")))
    }

    @Test
    fun shortcutCyclesAndReturnsToNormalSpeed() {
        val cycle = listOf(1, 8)
        assertEquals(8, FastForwardSpeeds.next(1, cycle))
        assertEquals(1, FastForwardSpeeds.next(8, cycle))
    }

    @Test
    fun speedSelectedFromMenuButExcludedFromCycleReturnsToNormal() {
        assertEquals(1, FastForwardSpeeds.next(16, listOf(1, 2)))
    }

    @Test
    fun menuResultAcceptsOnlySupportedSpeeds() {
        FastForwardSpeeds.supported.forEach { assertEquals(it, FastForwardSpeeds.validate(it)) }
        listOf(-1, 0, 3, 32, Int.MAX_VALUE).forEach { assertEquals(1, FastForwardSpeeds.validate(it)) }
    }

    @Test
    fun preferredSpeedAlwaysRepresentsFastForward() {
        listOf(1, 0, -1, 3, 32).forEach { assertEquals(2, FastForwardSpeeds.preferred(it)) }
        listOf(2, 4, 8, 16).forEach { assertEquals(it, FastForwardSpeeds.preferred(it)) }
    }

    @Test
    fun changingPreferredSpeedWhileDisabledKeepsNormalPlayback() {
        assertEquals(1, FastForwardSpeeds.select(1, 8))
    }

    @Test
    fun changingPreferredSpeedWhileEnabledUpdatesPlayback() {
        assertEquals(8, FastForwardSpeeds.select(4, 8))
    }

    @Test
    fun indicatorToggleStopsInsteadOfCyclingToAnotherFastSpeed() {
        listOf(2, 4, 8, 16).forEach { speed ->
            assertEquals(1, FastForwardSpeeds.toggle(speed, speed))
        }
    }

    @Test
    fun indicatorToggleResumesTheRememberedSpeed() {
        listOf(2, 4, 8, 16).forEach { remembered ->
            val stopped = FastForwardSpeeds.toggle(remembered, remembered)
            assertEquals(remembered, FastForwardSpeeds.toggle(stopped, remembered))
        }
    }
}
