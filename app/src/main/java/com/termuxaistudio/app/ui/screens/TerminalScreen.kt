package com.termuxaistudio.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun TerminalScreen() {
    var command by remember { mutableStateOf("") }
    val outputLines = remember {
        mutableStateListOf(
            "Termux AI Studio Terminal v1.1",
            "Sandbox + limited real command execution",
            "Type 'help' for commands | 'ai-install' for AI tools guide",
            ""
        )
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var isRunning by remember { mutableStateOf(false) }

    fun appendOutput(text: String) {
        text.lines().forEach { outputLines.add(it) }
        scope.launch {
            listState.animateScrollToItem(outputLines.size - 1)
        }
    }

    fun runCommand(cmd: String) {
        if (cmd.isBlank()) return
        outputLines.add("$ $cmd")
        isRunning = true

        scope.launch {
            val result = withContext(Dispatchers.IO) {
                executeCommand(cmd.trim())
            }
            appendOutput(result)
            outputLines.add("")
            isRunning = false
            command = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF161B22))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Terminal",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isRunning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = Color(0xFF58A6FF)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            IconButton(onClick = {
                outputLines.clear()
                outputLines.add("Terminal cleared.")
                outputLines.add("")
            }) {
                Icon(Icons.Default.Clear, contentDescription = "پاک کردن", tint = Color(0xFF8B949E))
            }
        }

        // Output
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            items(outputLines) { line ->
                Text(
                    text = line,
                    color = when {
                        line.startsWith("$") -> Color(0xFF58A6FF)
                        line.startsWith("Error") || line.startsWith("Command not") -> Color(0xFFF85149)
                        else -> Color(0xFF3FB950)
                    },
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }

        // Input
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
                enabled = !isRunning,
                textStyle = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(Color(0xFF58A6FF)),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { runCommand(command) }
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun executeCommand(cmd: String): String {
    return try {
        when {
            cmd == "help" || cmd == "?" -> """
Available commands:
  help, clear, echo, date, whoami, uname, pwd, ls, id
  ai-install   → راهنمای نصب هوش مصنوعی در Termux
  uname -a, date, id  → سعی در اجرای واقعی

Note: Full Linux environment requires Termux.
This app runs in Android sandbox with limited access.
            """.trimIndent()

            cmd == "clear" -> ""

            cmd == "ai-install" -> """
=== راهنمای نصب AI در Termux ===

1) OpenCode (پیشنهادی - رایگان):
   pkg install nodejs-lts
   npm install -g opencode-ai
   opencode

2) مدل رایگان داخل OpenCode:
   بعد از اجرا: /models
   مدل‌های Free را انتخاب کنید

3) DeepSeek (کیفیت بالا):
   از سایت deepseek.com کلید رایگان/ارزان بگیرید
   داخل OpenCode با /connect وصل کنید

4) برای عکس رایگان:
   از Cloudflare Workers AI استفاده کنید
            """.trimIndent()

            cmd.startsWith("echo ") -> cmd.removePrefix("echo ")

            cmd == "pwd" -> tryRealCommand(arrayOf("pwd")) ?: "/data/data/com.termuxaistudio.app/files"

            cmd == "whoami" -> tryRealCommand(arrayOf("whoami")) ?: "u0_aXXX"

            cmd == "id" -> tryRealCommand(arrayOf("id")) ?: "uid=XXXX(u0_aXXX) gid=XXXX"

            cmd == "date" -> tryRealCommand(arrayOf("date")) 
                ?: java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.US)
                    .format(java.util.Date())

            cmd == "uname" || cmd == "uname -a" ->
                tryRealCommand(arrayOf("uname", "-a")) ?: "Linux localhost 5.x.x-android aarch64"

            cmd == "ls" || cmd == "ls -la" ->
                tryRealCommand(arrayOf("ls", "-la")) ?: "projects\nstorage\ncache\nfiles"

            else -> {
                // Try real execution for other simple commands
                val parts = cmd.split(" ").toTypedArray()
                tryRealCommand(parts) ?: "Command not found or not allowed in sandbox: $cmd\nType 'help' or 'ai-install'"
            }
        }
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

private fun tryRealCommand(args: Array<String>): String? {
    return try {
        val process = ProcessBuilder(*args)
            .redirectErrorStream(true)
            .start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            output.appendLine(line)
        }
        process.waitFor()
        val result = output.toString().trim()
        if (result.isEmpty()) null else result
    } catch (e: Exception) {
        null
    }
}
