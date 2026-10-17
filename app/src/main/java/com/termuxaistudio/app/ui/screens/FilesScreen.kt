package com.termuxaistudio.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class FileItem(
    val name: String,
    val isDirectory: Boolean,
    val size: String = "",
    val icon: ImageVector
)

@Composable
fun FilesScreen() {
    val files = remember {
        listOf(
            FileItem("Android", true, icon = Icons.Default.Folder),
            FileItem("Download", true, icon = Icons.Default.Folder),
            FileItem("DCIM", true, icon = Icons.Default.Folder),
            FileItem("projects", true, icon = Icons.Default.Folder),
            FileItem("main.py", false, "1.2 KB", Icons.Default.Description),
            FileItem("README.md", false, "2.4 KB", Icons.Default.Description),
            FileItem("app.apk", false, "12.5 MB", Icons.Default.Android)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "مدیریت فایل",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* New folder */ }) {
                    Icon(Icons.Default.CreateNewFolder, contentDescription = "پوشه جدید")
                }
            }
        }

        // Path
        Text(
            text = "/storage/emulated/0",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // File list
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(files) { file ->
                FileRow(file)
            }
        }
    }
}

@Composable
fun FileRow(file: FileItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Open file or folder */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = file.icon,
            contentDescription = null,
            tint = if (file.isDirectory) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyLarge
            )
            if (file.size.isNotEmpty()) {
                Text(
                    text = file.size,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (file.isDirectory) {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
