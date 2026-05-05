package com.example.sporex_app.ui.community

import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporex_app.R
import com.example.sporex_app.network.*
import com.example.sporex_app.ui.navigation.BottomNavBar
import com.example.sporex_app.ui.navigation.TopBar
import com.example.sporex_app.ui.theme.SPOREX_AppTheme
import com.example.sporex_app.useraccount.UserSession
import com.example.sporex_app.utils.isDarkMode
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import coil.compose.AsyncImage

class CommunityHP : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkMode = isDarkMode(this)
            SPOREX_AppTheme(darkTheme = darkMode) {

                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                var postsState by remember { mutableStateOf<List<PostResponse>>(emptyList()) }
                var loading by remember { mutableStateOf(true) }
                var error by remember { mutableStateOf<String?>(null) }

                val currentUsername = UserSession.getUsername(context)

                fun loadPosts() {
                    scope.launch {
                        loading = true
                        error = null
                        try {
                            val res = RetrofitClient.api.getPosts()
                            if (res.isSuccessful) {
                                postsState = res.body().orEmpty()
                            } else {
                                error = "Failed to load posts (${res.code()})"
                            }
                        } catch (e: Exception) {
                            error = e.localizedMessage
                        } finally {
                            loading = false
                        }
                    }
                }

                LaunchedEffect(Unit) { loadPosts() }

                val createPostLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) {
                    loadPosts()
                }

                Scaffold(
                    topBar = { TopBar() },
                    bottomBar = { BottomNavBar(currentScreen = "community") },
                    floatingActionButton = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            FloatingActionButton(
                                onClick = {
                                    context.startActivity(
                                        Intent(context, AsthmaSociety::class.java)
                                    )
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text("More")
                            }

                            FloatingActionButton(
                                onClick = {
                                    createPostLauncher.launch(
                                        Intent(context, CreatePostActivity::class.java)
                                    )
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text(
                                    "+",
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                                )
                            }
                        }
                     }
                ) { padding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {

                        when {
                            loading -> {
                                CircularProgressIndicator()
                            }

                            error != null -> {
                                Text(error ?: "")
                            }

                            else -> {
                                CommunityScreen(
                                    posts = postsState,
                                    currentUsername = currentUsername,
                                    onAddReply = { postId, comment ->
                                        scope.launch {
                                            try {
                                                RetrofitClient.api.addReply(
                                                    postId,
                                                    CreateReplyRequest(
                                                        user_name = currentUsername,
                                                        content = comment
                                                    )
                                                )
                                                loadPosts()
                                            } catch (_: Exception) {}
                                        }
                                    },
                                    onDeletePost = { postId ->
                                        scope.launch {
                                            try {
                                                val res = RetrofitClient.api.deletePost(postId)

                                                if (res.isSuccessful) {
                                                    loadPosts()
                                                } else {
                                                    error = "Delete failed: ${res.code()}"
                                                }

                                            } catch (e: Exception) {
                                                error = e.message
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommunityScreen(
    posts: List<PostResponse>,
    currentUsername: String,
    onAddReply: (String, String) -> Unit,
    onDeletePost: (String) -> Unit
) {
    var selectedPost by remember { mutableStateOf<PostResponse?>(null) }
    var categoryFilter by remember { mutableStateOf<PostCategory?>(null) }
    var showOnlyMine by remember { mutableStateOf(false) }

    val filteredPosts = posts.filter { post ->
        val matchesUser = !showOnlyMine || post.user_name == currentUsername
        val matchesCategory =
            categoryFilter == null ||
                    post.category.lowercase() == categoryFilter?.name?.lowercase()
        matchesUser && matchesCategory
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            FilterChip("All", !showOnlyMine && categoryFilter == null) {
                showOnlyMine = false
                categoryFilter = null
            }

            FilterChip("My Posts", showOnlyMine) {
                showOnlyMine = true
                categoryFilter = null
            }

            FilterChip("Mould", categoryFilter == PostCategory.MOULD) {
                categoryFilter = PostCategory.MOULD
                showOnlyMine = false
            }

            FilterChip("Health", categoryFilter == PostCategory.HEALTH) {
                categoryFilter = PostCategory.HEALTH
                showOnlyMine = false
            }

            FilterChip("Misc", categoryFilter == PostCategory.MISC) {
                categoryFilter = PostCategory.MISC
                showOnlyMine = false
            }
        }


        Spacer(Modifier.height(16.dp))


        LazyColumn {
            items(filteredPosts, key = { it.id }) { post ->

                CommunityPostCard(
                    post = post,
                    showDelete = post.user_name == currentUsername,
                    onDelete = { onDeletePost(post.id) },
                    onViewFull = { selectedPost = post },
                    onLike = {}
                )

                Spacer(Modifier.height(12.dp))
            }
        }
    }

    selectedPost?.let { post ->
        AlertDialog(
            onDismissRequest = { selectedPost = null },
            confirmButton = {},
            text = {
                FullPostView(
                    post = post,
                    currentUsername = currentUsername,
                    onAddReply = { comment ->
                        onAddReply(post.id, comment)
                    }
                )
            }
        )
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun CommunityPostCard(
    post: PostResponse,
    showDelete: Boolean,
    onLike: () -> Unit,
    onDelete: () -> Unit,
    onViewFull: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {

            Text(post.user_name, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(post.content)
            Spacer(Modifier.height(10.dp))
            post.image_url?.let { url ->
                val fullImageUrl =
                    if (url.startsWith("http")) {
                        url
                    } else {
                        "https://sporex.onrender.com$url"
                    }

                Spacer(Modifier.height(8.dp))

                AsyncImage(
                    model = fullImageUrl,
                    contentDescription = "Post image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Row {

                TextButton(onClick = onLike) {
                    Text("Like")
                }

                TextButton(onClick = onViewFull) {
                    Text("Comments (${post.replies.size})")
                }

                if (showDelete) {
                    TextButton(onClick = onDelete) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun FullPostView(
    post: PostResponse,
    currentUsername: String,
    onAddReply: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }

    Column {

        Text(post.user_name, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(post.content)

        Spacer(Modifier.height(12.dp))
        Text("Comments", fontWeight = FontWeight.SemiBold)

        post.replies.forEach { reply ->
            Text("${reply.user_name}: ${reply.content}")
            Spacer(Modifier.height(4.dp))
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            label = { Text("Add comment...") }
        )

        Spacer(Modifier.height(4.dp))

        Button(
            onClick = {
                if (commentText.isNotBlank()) {
                    onAddReply(commentText)
                    commentText = ""
                }
            }
        ) {
            Text("Comment")
        }
    }
}

@Preview(showBackground = true)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
fun CommunityScreenPreview() {

    val samplePosts = listOf(
        PostResponse(
            id = "1",
            user_name = "PreviewUser",
            post_name = "Preview Post",
            content = "This is a sample post",
            created_at = "2026-05-01T12:00:00.000000",
            replies = emptyList(),
            category = "mould",
            image_url = null
        )
    )

    CommunityScreen(
        posts = samplePosts,
        currentUsername = "PreviewUser",
        onAddReply = { _, _ -> },
        onDeletePost = { _ -> }
    )
}