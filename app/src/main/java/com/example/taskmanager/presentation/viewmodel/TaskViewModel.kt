package com.example.taskmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.repository.TaskRepository
import com.example.taskmanager.util.AnalyticsLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _currentTask = MutableStateFlow<Task?>(null)
    val currentTask: StateFlow<Task?> = _currentTask.asStateFlow()

    fun loadTask(id: Int) {
        viewModelScope.launch {
            _currentTask.value = repository.getTaskById(id)
        }
    }

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            repository.getTasks().collectLatest { tasks ->
                _tasks.value = tasks
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.insertTask(task)
                analyticsLogger.logEvent("task_added", mapOf("task_id" to task.id))
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add task: ${e.message}"
                analyticsLogger.logError("add_task_failed", e)
            }
        }
    }

    suspend fun getTaskById(id: Int): Task? {
        return try {
            repository.getTaskById(id)
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load task: ${e.message}"
            analyticsLogger.logError("get_task_failed", e)
            null
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
                analyticsLogger.logEvent("task_updated", mapOf("task_id" to task.id))
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update task: ${e.message}"
                analyticsLogger.logError("update_task_failed", e)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
                analyticsLogger.logEvent("task_deleted", mapOf("task_id" to task.id))
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete task: ${e.message}"
                analyticsLogger.logError("delete_task_failed", e)
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Method to intentionally crash for testing Firebase Crashlytics
    fun triggerCrash() {
        throw RuntimeException("This is a test crash for Crashlytics")
    }

    // Method to trigger database error
    fun triggerDatabaseError() {
        viewModelScope.launch {
            try {
                // Trying to insert an invalid task
                repository.insertTask(Task(title = "", description = ""))
            } catch (e: Exception) {
                analyticsLogger.logError("database_error", e)
                _errorMessage.value = "Database error occurred"
            }
        }
    }
}