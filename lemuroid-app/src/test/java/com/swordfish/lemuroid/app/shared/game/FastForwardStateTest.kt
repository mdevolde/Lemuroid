package com.swordfish.lemuroid.app.shared.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FastForwardStateTest {
    @Test
    fun menuEnablesPlaybackAndOsd() {
        val state = FastForwardState(preferredSpeed = 8).setEnabled(true)
        assertTrue(state.enabled)
        assertEquals(8, state.frameSpeed)
    }

    @Test
    fun osdSuspendsPlaybackWithoutDisablingMenu() {
        val state = FastForwardState(preferredSpeed = 8).setEnabled(true).toggleOsd()
        assertTrue(state.enabled)
        assertTrue(state.suspended)
        assertEquals(1, state.frameSpeed)
    }

    @Test
    fun osdResumesTheSameSpeedWithoutChangingMenu() {
        val state = FastForwardState(preferredSpeed = 8).setEnabled(true).toggleOsd().toggleOsd()
        assertTrue(state.enabled)
        assertFalse(state.suspended)
        assertEquals(8, state.frameSpeed)
    }

    @Test
    fun menuDisablesAndResetsSuspendedOsd() {
        val state = FastForwardState(preferredSpeed = 4).setEnabled(true).toggleOsd().setEnabled(false)
        assertFalse(state.enabled)
        assertFalse(state.suspended)
        assertEquals(1, state.frameSpeed)
        assertEquals(4, state.preferredSpeed)
    }

    @Test
    fun menuReenableRestoresPlaybackAfterOsdWasSuspended() {
        val state =
            FastForwardState(preferredSpeed = 16).setEnabled(true).toggleOsd()
                .setEnabled(false).setEnabled(true)
        assertFalse(state.suspended)
        assertEquals(16, state.frameSpeed)
    }

    @Test
    fun osdCannotEnableTheFeatureWhenMenuIsOff() {
        val state = FastForwardState(preferredSpeed = 8).toggleOsd()
        assertFalse(state.enabled)
        assertEquals(1, state.frameSpeed)
    }

    @Test
    fun selectingSpeedWhileSuspendedKeepsMenuOnAndPlaybackAtNormalSpeed() {
        val state = FastForwardState().setEnabled(true).toggleOsd().selectSpeed(8)
        assertTrue(state.enabled)
        assertTrue(state.suspended)
        assertEquals(1, state.frameSpeed)
        assertEquals(8, state.toggleOsd().frameSpeed)
    }

    @Test
    fun selectingSpeedWhileDisabledDoesNotEnableMenuOrPlayback() {
        val state = FastForwardState().selectSpeed(16)
        assertFalse(state.enabled)
        assertEquals(1, state.frameSpeed)
        assertEquals(16, state.setEnabled(true).frameSpeed)
    }

    @Test
    fun selectingSpeedDuringPlaybackAppliesImmediately() {
        val state = FastForwardState().setEnabled(true).selectSpeed(4)
        assertTrue(state.enabled)
        assertEquals(4, state.frameSpeed)
    }

    @Test
    fun controllerSpeedSelectionClearsTemporarySuspension() {
        val suspended = FastForwardState().setEnabled(true).toggleOsd()
        assertEquals(4, suspended.setFrameSpeed(4).frameSpeed)
        assertFalse(suspended.setFrameSpeed(1).enabled)
    }
}
