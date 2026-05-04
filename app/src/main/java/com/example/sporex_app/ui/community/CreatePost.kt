package com.example.sporex_app.ui.community

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporex_app.R
import com.example.sporex_app.network.RetrofitClient
import com.example.sporex_app.ui.navigation.BottomNavBar
import com.example.sporex_app.ui.navigation.TopBar
import com.example.sporex_app.ui.theme.SPOREX_AppTheme
import coil.compose.AsyncImage
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sporex_app.network.CreatePostRequest
import com.example.sporex_app.network.PostCategory
import com.example.sporex_app.useraccount.UserSession
import com.example.sporex_app.utils.isDarkMode
import kotlinx.coroutines.launch

class CreatePostActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val darkMode = isDarkMode(this)
            SPOREX_AppTheme(darkTheme = darkMode) {

                Scaffold(
                    topBar = { TopBar() },
                    bottomBar = { BottomNavBar(currentScreen = "community") }
                ) { padding ->
                    CreatePostScreen(
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    var postContent by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()
    var selectedCategory by remember { mutableStateOf(PostCategory.MISC) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val currentUsername = UserSession.getUsername(context)

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Category", fontWeight = FontWeight.SemiBold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip("Mould", selectedCategory == PostCategory.MOULD) {
                selectedCategory = PostCategory.MOULD
            }

            FilterChip("Health", selectedCategory == PostCategory.HEALTH) {
                selectedCategory = PostCategory.HEALTH
            }

            FilterChip("Misc", selectedCategory == PostCategory.MISC) {
                selectedCategory = PostCategory.MISC
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Create Post",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            border = BorderStroke(
                2.dp,
                colors.onBackground
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "What's on your mind?",
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = postContent,
                    onValueChange = { postContent = it },
                    textStyle = androidx.compose.ui.text.TextStyle(color = colors.onSurface),
                    placeholder = {
                        Text(
                            "Share your mold experience or ask for advice...",
                            color = colors.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.onSurface.copy(alpha = 0.5f),
                        cursorColor = colors.primary
                    )
                )
                selectedImageUri?.let {
                    Spacer(Modifier.height(12.dp))
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }

                Button(onClick = { imagePicker.launch("image/*") }) {
                    Text("Add Photo")
                }

                if (error != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        scope.launch {
                            loading = true
                            error = null
                            try {
                                val res = RetrofitClient.api.createPost(
                                    CreatePostRequest(
                                        user_name = currentUsername,
                                        post_name = "Post",
                                        content = postContent,
                                        category = selectedCategory.name.lowercase()
                                    )
                                )

                                if (res.isSuccessful) {
                                    activity?.setResult(Activity.RESULT_OK)
                                    activity?.finish()
                                } else {
                                    error = "Failed to create post (${res.code()})"
                                }
                            } catch (e: Exception) {
                                error = "Network error: ${e.localizedMessage ?: "Unknown error"}"
                            } finally {
                                loading = false
                            }
                        }
                    },
                    enabled = postContent.isNotBlank() && !loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    )
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = colorResource(id = R.color.sporex_white)
                        )
                    } else {
                        Text("Post")
                    }
                }
            }
        }
    }
}

