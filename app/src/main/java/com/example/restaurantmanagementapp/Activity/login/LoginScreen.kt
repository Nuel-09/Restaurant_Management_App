package com.example.restaurantmanagementapp.Activity.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.ViewModel.LoginResult
import com.example.restaurantmanagementapp.ViewModel.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (username: String, role:String) -> Unit,
    onSignupClick: () -> Unit
) {
    val loginResult by viewModel.loginResult.collectAsState()
    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var passwordVisible by remember { mutableStateOf(false)    }
    var showSnackbar by remember { mutableStateOf(false)}
    var snackbarMessage by remember { mutableStateOf("")}

    // Handle login result and navigation
    LaunchedEffect(loginResult) {
        when (loginResult) {
            is LoginResult.Success -> {
                val result = loginResult as LoginResult.Success
                onLoginSuccess(result.username, result.role)
            }
            is LoginResult.Error -> {
                snackbarMessage = (loginResult as LoginResult.Error).message
                showSnackbar = true
            }
            else -> Unit
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF181A20))
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
                // Ui component for logo
                ConstraintLayout(modifier = Modifier
                    .padding(horizontal = 60.dp))
                {
                    val(logoBackground)=createRefs()
                    Box(
                        modifier = Modifier
                            .constrainAs(logoBackground) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .size(190.dp, 200.dp)
                            .background(color = Color(0xFFD5C6BC), shape = CircleShape)
                            .padding(horizontal = 32.dp)
                    ){
                        Image(painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize())
                    }
                }
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = Color.White)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.Gray,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description, tint = Color.White)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.Gray,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Buttons Row (Sign In and Create Account)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Left: Create Account
                Button(
                    onClick = { onSignupClick()},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .padding(bottom = 16.dp, top = 12.dp)
                        .fillMaxWidth(0.65f)
                        .height(50.dp)
                        .border(1.dp, Color.White, shape = RoundedCornerShape(50.dp))
                ) {
                    Text(
                        text = "Create Account",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
                // Right: Sign In
                Button(
                    onClick = { viewModel.login(username, password) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xF7E03F3A)),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 12.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Sign In",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }

        // Snackbar for notifications
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text("Dismiss", color = Color.White)
                    }
                },
                containerColor = Color(0xFF23262F)
            ) {
                Text(snackbarMessage, color = Color.White)
            }
        }
    }
}