package com.example.sporex_app.useraccount

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.sporex_app.R
import com.example.sporex_app.ui.community.CommunityHP
import com.example.sporex_app.ui.components.HistoryActivity
import com.example.sporex_app.ui.device.DeviceActivity
import com.example.sporex_app.ui.navigation.BottomNavBar
import com.example.sporex_app.ui.navigation.TopBar
import com.example.sporex_app.ui.theme.SPOREX_AppTheme
import com.example.sporex_app.utils.isDarkMode

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val context = this

            var usernameState by remember {
                mutableStateOf(UserSession.getUsername(context))
            }

            var imageState by remember {
                mutableStateOf(UserSession.getImage(context))
            }

            val editProfileLauncher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == RESULT_OK) {
                        usernameState = UserSession.getUsername(context)
                        imageState = UserSession.getImage(context)
                    }
                }

            val darkMode = remember { mutableStateOf(isDarkMode(context)) }

            SPOREX_AppTheme(darkTheme = darkMode.value) {
                ProfileScreen(
                    username = usernameState,
                    imageUri = imageState,
                    onHistoryClick = { startActivity(Intent(context, HistoryActivity::class.java)) },
                    onPostsClick = { startActivity(Intent(context, CommunityHP::class.java)) },
                    onDeviceClick = { startActivity(Intent(context, DeviceActivity::class.java)) },
                    onSettingsClick = { startActivity(Intent(context, UserSettings::class.java)) },
                    onEditProfileClick = {
                        val intent = Intent(context, EditProfileActivity::class.java)
                        editProfileLauncher.launch(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(
    username: String,
    imageUri: String?,
    onHistoryClick: () -> Unit,
    onPostsClick: () -> Unit,
    onDeviceClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        bottomBar = { BottomNavBar(currentScreen = "profile") },
        containerColor = MaterialTheme.colorScheme.primary
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(
                    bottom = padding.calculateBottomPadding(),
                    start = padding.calculateStartPadding(layoutDirection),
                    end = padding.calculateEndPadding(layoutDirection)
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                TopBar()

                ProfileContent(
                    username = username,
                    imageUri = imageUri,
                    onHistoryClick = onHistoryClick,
                    onPostsClick = onPostsClick,
                    onDeviceClick = onDeviceClick,
                    onSettingsClick = onSettingsClick,
                    onEditProfileClick = onEditProfileClick
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    username: String,
    imageUri: String?,
    onHistoryClick: () -> Unit,
    onPostsClick: () -> Unit,
    onDeviceClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.size(170.dp),
            contentAlignment = Alignment.BottomEnd
        ) {

            val imageUrl = imageUri?.let {
                if (it.startsWith("http")) it
                else "https://sporex.onrender.com$it"
            }

            if (!imageUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profilepicture),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onEditProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = username,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileActionCard("Community", Icons.Default.Article, onPostsClick)
                ProfileActionCard("History", Icons.Default.History, onHistoryClick)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileActionCard("My Device", Icons.Default.Devices, onDeviceClick)
                ProfileActionCard("Settings", Icons.Default.Settings, onSettingsClick)
            }
        }
    }
}

@Composable
private fun ProfileActionCard(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(160.dp)
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(16.dp))

            Text(
                label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}