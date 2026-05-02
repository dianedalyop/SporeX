package com.example.sporex_app.ui.alerts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.foundation.background
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import com.example.sporex_app.ui.navigation.BottomNavBar
import com.example.sporex_app.ui.navigation.TopBar
import com.example.sporex_app.ui.theme.SPOREX_AppTheme
import com.example.sporex_app.utils.isDarkMode

class NotificationsActivity : ComponentActivity() {

    private val CHANNEL_ID = "sporex_alerts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        // Trigger a high-priority alert on start for demo
        sendAlertNotification(
            "Ventilation Reminder",
            "Based on your settings: It's time to open a window for fresh air flow."
        )

        setContent {
            val darkMode = isDarkMode(this)
            // State-managed list to allow deletion
            var notifications by remember { mutableStateOf(getInitialNotifications()) }

            SPOREX_AppTheme(darkTheme = darkMode) {
                NotificationsScreen(
                    notifications = notifications,
                    onDelete = { item ->
                        notifications = notifications.filter { it != item }
                    }
                )
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SporeX Alerts"
            val descriptionText = "Urgent mold and air quality alerts"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendAlertNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Makes it a "Proper Alert"
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun getInitialNotifications() = listOf(
        NotificationItem("Ventilation Update", "Fresh air flow is optimized. Next check in 4h.", "Just now"),
        NotificationItem("Mold Detected", "Living Room: 85% confidence. Action required.", "12 mins ago"),
        NotificationItem("CO₂ Warning", "High levels detected in Kitchen.", "1 hour ago"),
        NotificationItem("System Check", "All sensors are online and calibrated.", "Yesterday")
    )
}

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onDelete: (NotificationItem) -> Unit
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar(currentScreen = "alerts") },
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
        ) {

            Text(
                text = "Alerts",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(notifications, key = { it.message + it.time }) { notification ->
                    NotificationCard(
                        notification = notification,
                        onDoubleTap = { onDelete(notification) }
                    )
                }
            }
        }
    }
}


@Composable
fun NotificationCard(notification: NotificationItem, onDoubleTap: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { onDoubleTap() })
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = notification.time,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
