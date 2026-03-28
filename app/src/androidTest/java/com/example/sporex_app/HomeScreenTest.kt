package com.example.sporex_app

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.sporex_app.ui.screens.HomeScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun cameraCard_callsUploadCallback_once() {
        var clicks = 0

        composeRule.setContent {
            HomeScreen(
                onUploadClick = { clicks++ },
                onProductsClick = {}
            )
        }

        // contentDescription = "Camera".
        composeRule.onNodeWithContentDescription("Camera").performClick()

        assertEquals(1, clicks)
    }
}
