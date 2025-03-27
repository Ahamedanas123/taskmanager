package com.example.taskmanager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanager.presentation.screens.AddEditTaskScreen
import com.example.taskmanager.presentation.screens.TaskDetailScreen
import com.example.taskmanager.presentation.screens.TaskListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "task_list"
    ) {
        composable("task_list") {
            TaskListScreen(navController = navController)
        }
        composable("add_task") {
            AddEditTaskScreen(navController = navController)
        }
        composable("add_task/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            AddEditTaskScreen(
                navController = navController,
                taskId = taskId
            )
        }
        composable("task_detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
                ?: return@composable
            TaskDetailScreen(
                navController = navController,
                taskId = taskId
            )
        }
    }
}