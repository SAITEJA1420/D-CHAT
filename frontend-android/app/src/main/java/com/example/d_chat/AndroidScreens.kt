package com.example.d_chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val pureWhiteBg = Color(0xFFFFFFFF)
val darkCharcoal = Color(0xFF1A1A1A)
val softLightGray = Color(0xFFF4F5F7)
val mutedGrayBorder = Color(0xFFD1D5DB)
val secondaryText = Color(0xFF6B7280)
val neonBlue = Color(0xFF007AFF)
@Composable
fun WelcomeScreen(onNavigate: (String) -> Unit){
    var gameStarted by remember { mutableStateOf(true) }
    LaunchedEffect(gameStarted) {
        delay(2000)
        gameStarted = false
        onNavigate("login")
    }
    if(gameStarted) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "D-CHAT",
                style = TextStyle(
                    color = darkCharcoal,
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 6.sp,
                    fontFamily = FontFamily.Monospace
                ),
                textAlign = TextAlign.Center

            )
        }
    }
}

@Composable
fun LoginScreen(onLoginSubmit:(String, String) -> Unit,
                onSignupSubmit:() -> Unit,
                loginMessage: String)
{
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pureWhiteBg)
            .padding(vertical = 48.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LOG IN TO D-CHAT",
            style = TextStyle(
                color = darkCharcoal,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                fontFamily = FontFamily.Monospace
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "INITIALIZE SECURE CHANNEL",
            style = TextStyle(
                color = secondaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        TextField(
            value = userName,
            onValueChange = { userName = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, mutedGrayBorder), RoundedCornerShape(4.dp)),
            label = {
                Text(
                    text = "Username",
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                )
            },
            textStyle = TextStyle(color = darkCharcoal, fontFamily = FontFamily.Monospace, fontSize = 16.sp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = softLightGray,
                unfocusedContainerColor = softLightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = darkCharcoal,
                unfocusedLabelColor = secondaryText
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, mutedGrayBorder), RoundedCornerShape(4.dp)),
            label = {
                Text(
                    text = "Password",
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                )
            },
            textStyle = TextStyle(color = darkCharcoal, fontFamily = FontFamily.Monospace, fontSize = 16.sp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = softLightGray,
                unfocusedContainerColor = softLightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = darkCharcoal,
                unfocusedLabelColor = secondaryText
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, Color.Blue), RoundedCornerShape(4.dp))
                .clickable {
                    onLoginSubmit(userName, password)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Log in >",
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Monospace
                )
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Box(
            modifier = Modifier
                .height(54.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
                .clickable {onSignupSubmit()}
                .border(BorderStroke(1.dp, Color.Blue), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp)
                .wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "New User,Sign Up",
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Monospace
                )
            )
        }

        Spacer(modifier = Modifier.height(26.dp))


        LaunchedEffect(Unit) {}
        Text(
            text = loginMessage,
            style = TextStyle(color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SignupScreen(onSignupSubmit: (String, String) -> Unit,
                 signupMessage: String){
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pureWhiteBg)
            .padding(vertical = 48.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Welcome TO D-CHAT",
            style = TextStyle(
                color = darkCharcoal,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                fontFamily = FontFamily.Monospace
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = userName,
            onValueChange = { userName = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, mutedGrayBorder), RoundedCornerShape(4.dp)),
            label = {
                Text(
                    text = "Enter your Username",
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                )
            },
            textStyle = TextStyle(color = darkCharcoal, fontFamily = FontFamily.Monospace, fontSize = 16.sp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = softLightGray,
                unfocusedContainerColor = softLightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = darkCharcoal,
                unfocusedLabelColor = secondaryText
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, mutedGrayBorder), RoundedCornerShape(4.dp)),
            label = {
                Text(
                    text = "Password",
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                )
            },
            textStyle = TextStyle(color = darkCharcoal, fontFamily = FontFamily.Monospace, fontSize = 16.sp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = softLightGray,
                unfocusedContainerColor = softLightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = darkCharcoal,
                unfocusedLabelColor = secondaryText
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        Box(
            modifier = Modifier
                .height(54.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
                .clickable {
                    onSignupSubmit(userName, password)
                }
                .border(BorderStroke(1.dp, Color.Blue), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp)
                .wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign Up",
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Monospace
                )
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = signupMessage,
            style = TextStyle(color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NewChat(allUsers: List<User>,
            onUsernameSubmit: (String) -> Unit,
            onIdSubmit: (Int)-> Unit){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = pureWhiteBg),
        contentPadding = PaddingValues(vertical = 28.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Handles all item spacing automatically!
    ) {
        items(allUsers) { user ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(pureWhiteBg, RoundedCornerShape(4.dp))
                    .border(BorderStroke(1.dp, neonBlue), RoundedCornerShape(4.dp))
                    .clickable {
                        onUsernameSubmit(user.username)
                        onIdSubmit(user.id)
                    }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = user.username,
                    style = TextStyle(
                        color = darkCharcoal,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(user: (String), onMessageSubmit: (String) -> Unit, viewModel: DChatViewModel){
    var message by remember { mutableStateOf("") }
    LaunchedEffect(viewModel.userId, viewModel.receiverId) {
        if(viewModel.userId != 0 && viewModel.receiverId != 0){
            viewModel.fetchChatHistory(viewModel.userId, viewModel.receiverId)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text(text = user,
                            style = TextStyle(
                                color = darkCharcoal,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        placeholder = { Text("Text here") },
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (message.isNotBlank()) {
                                onMessageSubmit(message)

                                message = "" // Clear the field
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.senddd),
                            contentDescription = "send icon",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Main body content goes here
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = pureWhiteBg)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(viewModel.chatHistory) { msg ->
                    val isSentByMe = msg.sender_id == viewModel.userId
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if(isSentByMe){
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF6495ED), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                                    .align(Alignment.End)
                            ) {
                                Column {
                                    Text(text = msg.content, fontSize = 16.sp)
                                }
                            }
                        }
                        if(!isSentByMe){
                            Box(
                                modifier = Modifier

                                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                                    .align(Alignment.Start)
                            ) {
                                Column {
                                    Text(text = msg.content, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}