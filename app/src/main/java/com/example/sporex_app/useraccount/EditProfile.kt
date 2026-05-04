package com.example.sporex_app.useraccount

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporex_app.R
import com.example.sporex_app.ui.theme.SPOREX_AppTheme
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.sporex_app.ui.navigation.BottomNavBar
import com.example.sporex_app.ui.navigation.TopBar
import kotlinx.coroutines.launch

class EditProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUsername = UserSession.getUsername(this)
        val currentEmail = UserSession.getEmail(this)
        val currentImage = UserSession.getImage(this)

        val isDarkMode = com.example.sporex_app.utils.isDarkMode(this)

        setContent {
            SPOREX_AppTheme(darkTheme = isDarkMode) {

                EditProfileScreen(
                    currentUsername = currentUsername,
                    currentEmail = currentEmail,
                    currentImage = currentImage,
                    onBack = { finish() },

                    onSave = { updatedUsername, updatedEmail, imageUri ->

                        val api = com.example.sporex_app.network.RetrofitClient.api
                        val context = this@EditProfileActivity

                        lifecycleScope.launch {
                            try {
                                val response = UserRepository.updateProfile(
                                    api = api,
                                    context = context,
                                    email = updatedEmail,
                                    username = updatedUsername,
                                    imageUri = imageUri
                                )

                                if (response.isSuccessful) {

                                    val body = response.body()

                                    UserSession.saveUser(
                                        context,
                                        body?.username ?: updatedUsername,
                                        body?.email ?: updatedEmail
                                    )

                                    body?.profile_image?.let {
                                        UserSession.saveImage(context, it)
                                    }

                                    val resultIntent = Intent().apply {
                                        putExtra("username", body?.username ?: updatedUsername)
                                        putExtra("email", body?.email ?: updatedEmail)
                                        putExtra("image", body?.profile_image)
                                    }

                                    setResult(Activity.RESULT_OK, resultIntent)
                                    finish()
                                } else {
                                    Toast.makeText(context, "Update failed: ${response.code()}", Toast.LENGTH_LONG).show()
                                }

                            }
                            catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EditProfileScreen(
    currentUsername: String,
    currentEmail: String,
    currentImage: String?,
    onBack: () -> Unit,
    onSave: (String, String, Uri?) -> Unit
) {
    var username by remember { mutableStateOf(currentUsername) }
    var email by remember { mutableStateOf(currentEmail) }
    var showError by remember { mutableStateOf(false) }

    val context = LocalContext.current

     var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            profileImageUri = uri
        }
    }

    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar(currentScreen = "settings") },
        containerColor = colors.background
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }

                Text(
                    "Edit Profile",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

             Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {

                val imageUrl = currentImage?.let {
                    if (it.startsWith("http")) it
                    else "https://sporex.onrender.com$it"
                }

                when {
                     profileImageUri != null -> {
                        Image(
                            painter = rememberAsyncImagePainter(profileImageUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                     !imageUrl.isNullOrEmpty() -> {
                         Image(
                             painter = rememberAsyncImagePainter(imageUrl),
                             contentDescription = null,
                             contentScale = ContentScale.Crop,
                             modifier = Modifier
                                 .fillMaxSize()
                                 .clip(CircleShape)
                         )
                    }

                     else -> {
                        Image(
                            painter = painterResource(R.drawable.profilepicture),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            RoundedTextField(
                value = username,
                label = "Username",
                onValueChange = { username = it },
                colors = colors
            )

            Spacer(Modifier.height(12.dp))

            RoundedTextField(
                value = email,
                label = "Email",
                onValueChange = { email = it },
                colors = colors
            )

            if (showError) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Fields cannot be empty",
                    color = colors.error
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (username.isBlank() || email.isBlank()) {
                        showError = true
                    } else {
                        showError = false

                        try {
                            onSave(username, email, profileImageUri)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun RoundedTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    colors: ColorScheme
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = colors.onSurface) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.onSurface.copy(alpha = 0.5f),
            cursorColor = colors.onSurface,
            focusedTextColor = colors.onSurface,
            unfocusedTextColor = colors.onSurface,
            focusedContainerColor = colors.surface.copy(alpha = 0.25f),
            unfocusedContainerColor = colors.surface.copy(alpha = 0.18f)
        )
    )
}

@Composable
fun RoundedPasswordField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    colors: ColorScheme
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = colors.onBackground) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.onBackground,
            unfocusedBorderColor = colors.onBackground.copy(alpha = 0.5f),
            cursorColor = colors.onBackground,
            focusedTextColor = colors.onBackground,
            unfocusedTextColor = colors.onBackground,
            focusedContainerColor = colors.surface.copy(alpha = 0.25f),
            unfocusedContainerColor = colors.surface.copy(alpha = 0.18f)
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordScreen(
    onBack: () -> Unit,
    onSave: (String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordStrength by remember { mutableStateOf(0f) }

    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar(currentScreen = "settings") },
        containerColor = colors.background
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // --- Back button row ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.onBackground
                    )
                }
                Text(
                    text = "Change Password",
                    color = colors.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Text(
                text = "Keep your account secure by updating your password.",
                color = colors.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(24.dp))

            RoundedPasswordField(
                value = currentPassword,
                label = "Current Password",
                onValueChange = { currentPassword = it },
                colors = colors
            )

            Spacer(Modifier.height(16.dp))

            RoundedPasswordField(
                value = newPassword,
                label = "New Password",
                onValueChange = {
                    newPassword = it
                    passwordStrength = calculatePasswordStrength(it)
                },
                colors = colors
            )

            Spacer(Modifier.height(12.dp))

            PasswordStrengthBar(strength = passwordStrength, colors = colors)

            Spacer(Modifier.height(16.dp))

            RoundedPasswordField(
                value = confirmPassword,
                label = "Confirm New Password",
                onValueChange = { confirmPassword = it },
                colors = colors
            )

            errorMessage?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = colors.error, fontSize = 14.sp)
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    when {
                        currentPassword.isBlank() ||
                                newPassword.isBlank() ||
                                confirmPassword.isBlank() ->
                            errorMessage = "All fields are required"

                        newPassword != confirmPassword ->
                            errorMessage = "New passwords do not match"

                        newPassword.length < 6 ->
                            errorMessage = "Password must be at least 6 characters"

                        else -> {
                            errorMessage = null
                            onSave(newPassword)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Text("Update Password", fontSize = 17.sp)
            }
        }
    }
}

@Composable
fun PasswordStrengthBar(strength: Float, colors: androidx.compose.material3.ColorScheme) {
    val animatedWidth by animateFloatAsState(
        targetValue = strength,
        label = "strengthAnim"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(colors.surface.copy(alpha = 0.25f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedWidth)
                .background(
                    when {
                        strength < 0.34f -> colors.error
                        strength < 0.67f -> colors.secondary
                        else -> colors.primary
                    }
                )
                .clip(RoundedCornerShape(50.dp))
        )
    }
}


fun calculatePasswordStrength(password: String): Float {
    var score = 0

    if (password.length >= 6) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++

    return score / 3f
}

class EditPasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val darkMode = com.example.sporex_app.utils.isDarkMode(this)

        setContent {
            SPOREX_AppTheme(darkTheme = darkMode) {
                EditPasswordScreen(
                    onBack = { finish() },
                    onSave = { newPassword ->
                        val resultIntent = Intent().apply {
                            putExtra("new_password", newPassword)
                        }

                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                )
            }
        }
    }
}