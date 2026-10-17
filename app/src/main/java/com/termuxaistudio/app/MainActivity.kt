package com.termuxaistudio.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.termuxaistudio.app.ui.screens.*
import com.termuxaistudio.app.ui.theme.TermuxAIStudioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TermuxAIStudioTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val titleRes: Int, val icon: ImageVector) {
    object Terminal : Screen("terminal", R.string.tab_terminal, Icons.Default.Terminal)
    object Chat : Screen("chat", R.string.tab_chat, Icons.Default.SmartToy)
    object Editor : Screen("editor", R.string.tab_editor, Icons.Default.Code)
    object Files : Screen("files", R.string.tab_files, Icons.Default.Folder)
}

val screens = listOf(
    Screen.Terminal,
    Screen.Chat,
    Screen.Editor,
    Screen.Files
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.titleRes)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Terminal.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Terminal.route) { TerminalScreen() }
            composable(Screen.Chat.route) { ChatScreen() }
            composable(Screen.Editor.route) { EditorScreen() }
            composable(Screen.Files.route) { FilesScreen() }
        }
    }
}
