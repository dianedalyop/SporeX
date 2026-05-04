package com.example.sporex_app.ui.device

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sporex_app.ui.navigation.BottomNavBar
import com.example.sporex_app.ui.navigation.TopBar

@Composable
fun TestConnectionScreen(
    deviceName: String,
    firmwareVersion: String = "2.3.1",
    connectionStatus: Boolean = true,
    onReconnectClick: () -> Unit = {}
) {
    val model = deviceName

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar(currentScreen = "device") },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Header
            Text(
                text = model,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Connection Diagnostics",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            // Status Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Device Status",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Status indicator (cleaner than emojis everywhere)
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                if (connectionStatus) "Connected" else "Disconnected"
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (connectionStatus)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else
                                MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                            labelColor = if (connectionStatus)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                    )

                    Divider(color = MaterialTheme.colorScheme.outlineVariant)

                    DetailRow("Device Name", deviceName)
                    DetailRow("Model", model)
                    DetailRow("Firmware", firmwareVersion)

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onReconnectClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Reconnect Device")
                    }
                }
            }
        }
    }
}


@Composable
fun TestConnectionRoute(
    repo: DeviceRepository,
    onBack: () -> Unit
) {
    TestConnectionScreen(
        deviceName = repo.getDeviceName(),
        connectionStatus = true,
        onReconnectClick = {
            println("Reconnecting device…")
        }
    )
}