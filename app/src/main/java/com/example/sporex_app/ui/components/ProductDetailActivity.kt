package com.example.sporex_app.ui.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sporex_app.network.ProductDetail
import com.example.sporex_app.network.RetrofitClient
import com.example.sporex_app.ui.navigation.BottomNavBar
import com.example.sporex_app.ui.navigation.TopBar
import com.example.sporex_app.ui.theme.SPOREX_AppTheme
import com.example.sporex_app.utils.isDarkMode

class ProductDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productId = intent.getStringExtra("product_id") ?: "vinegar"

        setContent {
            val darkMode = isDarkMode(this)

            SPOREX_AppTheme(darkTheme = darkMode) {
                ProductDetailScreen(productId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailScreen(productId: String) {

    val context = LocalContext.current

    var detail by remember { mutableStateOf<ProductDetail?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val sporexGreen = MaterialTheme.colorScheme.primary

    LaunchedEffect(productId) {
        try {
            val res = RetrofitClient.api.getProductDetail(productId)
            if (res.isSuccessful) detail = res.body()
            else error = "Failed to load (${res.code()})"
        } catch (e: Exception) {
            error = "Network error: ${e.localizedMessage}"
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar(currentScreen = "products") },
        containerColor = sporexGreen
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(sporexGreen)
        ) {

            // 🔙 BACK BUTTON + PAGE TITLE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { (context as? ComponentActivity)?.finish() }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Method & Safety",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {

                loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(error!!, color = Color.White)
                    }
                }

                detail != null -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // MAIN CARD
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {

                                Text(
                                    detail!!.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.Black
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    "Best for: ${detail!!.best_for}",
                                    color = Color.DarkGray
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    "Safety: ${detail!!.warning}",
                                    color = Color(0xFFD32F2F)
                                )
                            }
                        }

                        Text(
                            "Steps",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )

                        detail!!.steps.forEachIndexed { index, step ->

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${index + 1}. $step",
                                    modifier = Modifier.padding(14.dp),
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
