package com.example.datastoreexample.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datastoreexample.data.UserStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
    // State variables to store user input
    val userName = remember { mutableStateOf(TextFieldValue()) }
    val userPassword = remember { mutableStateOf(TextFieldValue()) }
    val saveData = remember { mutableStateOf(false) }  // Checkbox state

    val context = LocalContext.current
    val store = UserStore(context)

    // Getting the previously saved token if it exists
    val tokenUser = store.getAccessToken.collectAsState(initial = "")
    val tokenpswd = store.getAccessToken.collectAsState(initial = "")

    LaunchedEffect(tokenUser.value) {
        if (tokenUser.value.isNotEmpty()) {
            userName.value = TextFieldValue(tokenUser.value)
        }
    }
    LaunchedEffect(tokenpswd.value) {
        if (tokenpswd.value.isNotEmpty()) {
            userPassword.value = TextFieldValue(tokenpswd.value)
        }
    }

    // Column to arrange UI elements vertically
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(40.dp)
    ) {
        // Welcome message
        Text(
            text = "Hello,\nWelcome to the login page", fontSize = 25.sp, color = Color.Blue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 50.dp, 0.dp, 0.dp)
        )

        // Username input field
        OutlinedTextField(
            value = userName.value, onValueChange = {
                userName.value = it
            },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person")
            },
            label = {
                Text(text = "username")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
        )

        // Password input field
        OutlinedTextField(
            value = userPassword.value, onValueChange = {
                userPassword.value = it
            },
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = "password")
            },
            label = {
                Text(text = "password")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        // Checkbox for saving data
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp) // Optional padding to adjust spacing
        ) {
            Checkbox(
                checked = saveData.value,
                onCheckedChange = { saveData.value = it }
            )
            Text(
                text = "Save my data",
                modifier = Modifier.padding(start = 8.dp) // Adjust this padding if needed
            )
        }
        // Login button
        OutlinedButton(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    store.saveToken(userName.value.text, userPassword.value.text, saveData.value)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 25.dp, 0.dp, 0.dp)
        ) {
            Text(
                text = "Login",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}
