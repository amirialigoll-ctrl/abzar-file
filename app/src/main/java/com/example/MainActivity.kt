package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.AIAssistantScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ToolDetailScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val navController = rememberNavController()
        
        NavHost(navController = navController, startDestination = "dashboard") {
            composable("dashboard") {
                DashboardScreen(
                    onNavigateToTool = { toolId ->
                        if (toolId == "ai_assistant") {
                            navController.navigate("ai_assistant")
                        } else {
                            navController.navigate("tool/$toolId")
                        }
                    }
                )
            }
            composable("tool/{toolId}") { backStackEntry ->
                val toolId = backStackEntry.arguments?.getString("toolId") ?: return@composable
                ToolDetailScreen(
                    toolId = toolId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("ai_assistant") {
                AIAssistantScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
      }
    }
  }
}

