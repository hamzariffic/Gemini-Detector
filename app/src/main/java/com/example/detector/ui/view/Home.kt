package com.example.detector.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.detector.ui.viewModel.HomeScreenViewModel

@Composable
fun MainScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val searchText by viewModel.searchText.collectAsState()
    val response by viewModel.response.collectAsState()

    Scaffold(
        topBar = {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.inversePrimary),
            ) {
                Text("AI Assistant")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.ChatBubble, contentDescription = "Chats") },
                    label = { Text("Chats") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {}
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.interactWithAI() },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Upload, contentDescription = "Upload")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.padding(16.dp)) {
                ChatInputField(searchText, viewModel::onSearchTextChange, viewModel::interactWithAI)
                response?.let {
                    Text(it.content, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }
}

@Composable
fun ChatInputField(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    var textState by remember { mutableStateOf(TextFieldValue(searchText)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = textState,
            onValueChange = {
                textState = it
                onSearchTextChange(it.text)
            },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        IconButton(onClick = onSendClick) {
            Icon(Icons.Filled.Send, contentDescription = "Send")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
