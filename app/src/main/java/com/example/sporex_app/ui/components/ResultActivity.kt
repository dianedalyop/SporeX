package com.example.sporex_app.ui.components

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.sporex_app.ui.navigation.BottomNavBar
import com.example.sporex_app.ui.navigation.TopBar
import com.example.sporex_app.ui.theme.SPOREX_AppTheme
import com.example.sporex_app.utils.isDarkMode

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mouldDetected = intent.getBooleanExtra("mould_detected", false)
        val maxConfidence = intent.getDoubleExtra("max_confidence", 0.0)
        val imageUrl = intent.getStringExtra("image_url").orEmpty()
        val message = intent.getStringExtra("message").orEmpty()

        setContent {
            val darkMode = isDarkMode(this)

            SPOREX_AppTheme(darkTheme = darkMode) {
                Scaffold(
                    bottomBar = { BottomNavBar(currentScreen = "camera") },
                    containerColor = MaterialTheme.colorScheme.surface
                ) { padding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF06A546)) // 🟢 BIG SPOREX GREEN BACKGROUND
                            .padding(bottom = padding.calculateBottomPadding())
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {

                            TopBar()

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(16.dp),
                                    tonalElevation = 2.dp
                                ) {
                                    MoldResultScreen(
                                        mouldDetected = mouldDetected,
                                        maxConfidence = maxConfidence,
                                        imageUrl = imageUrl,
                                        message = message
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MoldResultScreen(
        mouldDetected: Boolean,
        maxConfidence: Double,
        imageUrl: String,
        message: String
    ) {
        val context = LocalContext.current

        val fullImageUrl = if (imageUrl.startsWith("http")) imageUrl
        else "https://sporex.onrender.com$imageUrl"

        val confidencePercent = (maxConfidence * 100).toInt()

        val resultTitle = if (mouldDetected)
            "Possible Mould Detected"
        else "No Mould Detected"

        val resultSubtitle = when {
            !mouldDetected -> "The model did not detect mould in this image."
            maxConfidence >= 0.7 -> "High confidence: $confidencePercent%"
            maxConfidence >= 0.4 -> "Moderate confidence: $confidencePercent%"
            else -> "Low confidence: $confidencePercent%"
        }

        val adviceText = when {
            !mouldDetected -> "No mould detected. Retake if unsure."
            maxConfidence >= 0.7 -> "Likely mould present. Improve ventilation."
            maxConfidence >= 0.4 -> "Possible mould. Retake in better lighting."
            else -> "Low confidence result. Try another angle."
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // 🔴 RESULT BANNER
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (mouldDetected)
                            Color(0xFFD32F2F) // 🔴 RED
                        else
                            Color(0xFFE8F5E9),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {

                Icon(
                    imageVector = if (mouldDetected)
                        Icons.Default.Warning
                    else
                        Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = resultTitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )

                    Text(
                        text = resultSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // 🖼 IMAGE
            if (imageUrl.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(fullImageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // 📊 SUMMARY
            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    Text("Analysis Summary", style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(4.dp))

                    Text(message.ifBlank { "Prediction complete" })
                    Text("Confidence: $confidencePercent%")
                }
            }

            // 🧠 REMEDIES
            Text("Suggested Remedies", style = MaterialTheme.typography.titleMedium)

            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    Text("Recommended Action")

                    Spacer(Modifier.height(4.dp))

                    Text(adviceText)
                }
            }

            // 🔵 TRY AGAIN BUTTON
            Button(
                onClick = {
                    context.startActivity(Intent(context, UploadActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3), // 🔵 BLUE
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Try Another Image")
            }

            // ⚫ VIEW MORE BUTTON
            Button(
                onClick = {
                    context.startActivity(Intent(context, ProductsActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // ⚫ BLACK
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("View More Remedies")
            }
        }
    }
}