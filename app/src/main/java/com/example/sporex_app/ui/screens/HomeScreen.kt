package com.example.sporex_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sporex_app.ui.navigation.TopBar
import androidx.compose.foundation.layout.statusBarsPadding


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    onUploadClick: () -> Unit,
    onProductsClick: () -> Unit
) {


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF06A546))

    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            TopBar()
            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF06A546),
                shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, bottom = 12.dp)
                ) {
//                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Welcome Back!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    PreviousCaseCard()

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Scan For Mould",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    CameraCard(onUploadClick = onUploadClick)


                    // Optional spacing so it doesn’t hug the bottom
                  //  Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun CameraCard(onUploadClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clickable { onUploadClick() }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Camera",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(120.dp)
                            .offset(y = 6.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun PreviousCaseCard() {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Previous Case",
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "65%",
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "SPOREX has detected 65% exposure of Trichoderma in your home.",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Click for more information",
                    color = Color(0xFF06A546),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF06A546), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "View Case",
                    tint = Color.White
                )
            }
        }
    }
}
