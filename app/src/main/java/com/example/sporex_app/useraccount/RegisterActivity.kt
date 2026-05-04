package com.example.sporex_app.useraccount
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.sporex_app.network.RegisterRequest
import com.example.sporex_app.network.RetrofitClient
import com.example.sporex_app.ui.theme.SPOREX_AppTheme
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.sporex_app.MainActivity
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SPOREX_AppTheme {
                RegisterScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    val coroutineScope = rememberCoroutineScope()
    val apiService = RetrofitClient.api
    val context = LocalContext.current
    val activity = context as? RegisterActivity


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    val sharedPrefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    sharedPrefs.edit()
        .putString("user_email", email)
        .apply()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Register",
                        color = Color(0xFF06A546)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF06A546)
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF040F0F), // sporex_black
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    Log.d("REGISTER", "Button clicked")
                    coroutineScope.launch {
                        try {
                            val response = apiService.registerUser(
                                RegisterRequest(
                                    email = email,
                                    password = password,
                                    username = username
                                )
                            )

                            if (response.isSuccessful) {

                                context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("user_email", email)
                                    .apply()

                                Toast.makeText(
                                    context,
                                    "Account created successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }

                                context.startActivity(intent)


                            } else {
                                Log.e("REGISTER", "Error: ${response.code()}")

                                Toast.makeText(
                                    context,
                                    "Registration failed (${response.code()})",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: Exception) {
                            Log.e("REGISTER", "Exception: ${e.message}")

                            Toast.makeText(
                                context,
                                "Something went wrong. Try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF06A546), // sporex_green
                    contentColor = Color.White
                )
            ) {
                Text("Register", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Already have an account? Login",
                fontSize = 16.sp,
                color = Color(0xFF06A546), // clickable green
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    context.startActivity(
                        Intent(context, LoginActivity::class.java)
                    )
                }
            )
        }
    }
}