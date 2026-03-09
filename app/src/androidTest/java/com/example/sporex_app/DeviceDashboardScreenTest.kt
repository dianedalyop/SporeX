package com.example.sporex_app

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.sporex_app.ui.device.DeviceDashboardScreen
import com.example.sporex_app.ui.theme.SPOREX_AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DeviceDashboardScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun basicReadings_card_togglesExpandedState() {
        composeRule.setContent {
            SPOREX_AppTheme(dynamicColor = false) {
                DeviceDashboardScreen(
                    deviceName = "Test Device",
                    onManageDeviceClick = {},
                    onCreateDeviceClick = {}
                )
            }
        }

        // Expanded by default. Note: StatRow calls .uppercase() on the label.
        // "Humidity" in code becomes "HUMIDITY" in UI
        composeRule.onNodeWithText("HUMIDITY").assertExists()

        // Click card header to collapse
        composeRule.onNodeWithText("Basic Readings").performClick()
        composeRule.onNodeWithText("HUMIDITY").assertDoesNotExist()

        // Click again to expand
        composeRule.onNodeWithText("Basic Readings").performClick()
        composeRule.onNodeWithText("HUMIDITY").assertExists()
    }

    @Test
    fun manageDevice_callsCallback_once() {
        var clicks = 0

        composeRule.setContent {
            SPOREX_AppTheme(dynamicColor = false) {
                DeviceDashboardScreen(
                    deviceName = "Test Device",
                    onManageDeviceClick = { clicks++ },
                    onCreateDeviceClick = {}
                )
            }
        }

        composeRule.onNodeWithText("Manage Device").assertExists().performClick()
        assertEquals(1, clicks)
    }
}