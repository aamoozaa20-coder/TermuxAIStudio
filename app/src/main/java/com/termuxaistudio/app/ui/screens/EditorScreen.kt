package com.termuxaistudio.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditorScreen() {
    var code by remember {
        mutableStateOf(
            """def hello():
    print("Hello from Termux AI Studio!")

if __name__ == "__main__":
    hello()
""".trimIndent()
        )
    }
    var fileName by remember { mutableStateOf("main.py") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Toolbar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fileName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Save later */ }) {
                    Icon(Icons.Default.Save, contentDescription = "ذخیره")
                }
                IconButton(onClick = { /* Run later */ }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "اجرا")
                }
            }
        }

        // Editor
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF0D1117))
                .padding(12.dp)
        ) {
            BasicTextField(
                value = code,
                onValueChange = { code = it },
                textStyle = TextStyle(
                    color = Color(0xFFE6EDF3),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                cursorBrush = SolidColor(Color(0xFF58A6FF)),
                modifier = Modifier.fillMaxSize()
            )
        }

        // Status bar
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text(
                text = "Python • UTF-8 • Ln 1, Col 1",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
