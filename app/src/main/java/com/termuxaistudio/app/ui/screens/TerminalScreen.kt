package com.termuxaistudio.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun TerminalScreen() {
    var command by remember { mutableStateOf("") }
    val outputLines = remember { mutableStateListOf("Termux AI Studio Terminal v1.0", "Type commands below (local sandbox)", "") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(8.dp)
    ) {
        // Output area
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(outputLines) { line ->
                Text(
                    text = line,
                    color = Color(0xFF3FB950),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }

        // Input area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF161B22))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$ ",
                color = Color(0xFF58A6FF),
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )
            BasicTextField(
                value = command,
                onValueChange = { command = it },
                textStyle = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(Color(0xFF58A6FF)),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (command.isNotBlank()) {
                            outputLines.add("$ $command")
                            // Simple local command simulation for now
                            val result = executeLocalCommand(command.trim())
                            outputLines.add(result)
                            outputLines.add("")
                            command = ""
                            scope.launch {
                                listState.animateScrollToItem(outputLines.size - 1)
                            }
                        }
                    }
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun executeLocalCommand(cmd: String): String {
    return when {
        cmd == "help" || cmd == "?" -> """
Available commands (sandbox):
  help, clear, echo, date, whoami, uname, ls, pwd
Note: Full Termux integration coming in next versions.
        """.trimIndent()
        cmd == "clear" -> {
            // handled outside
            ""
        }
        cmd.startsWith("echo ") -> cmd.removePrefix("echo ")
        cmd == "date" -> java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        cmd == "whoami" -> "user@termux-ai-studio"
        cmd == "uname" || cmd == "uname -a" -> "Linux Android (Termux AI Studio sandbox)"
        cmd == "pwd" -> "/data/data/com.termuxaistudio.app/files"
        cmd == "ls" || cmd == "ls -la" -> "projects/\nstorage/\nreadme.md"
        else -> "Command not found in sandbox: $cmd\nType 'help' for available commands."
    }
}
