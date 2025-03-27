package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.TaskDao
import com.example.taskmanager.data.mapper.toDomain
import com.example.taskmanager.data.mapper.toDto
import com.example.taskmanager.data.mapper.toEntity
import com.example.taskmanager.data.remote.api.TaskApi
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val localDataSource: TaskDao,
    private val remoteDataSource: TaskApi
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return localDataSource.getTasks().map { tasks ->
            tasks.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return localDataSource.getTaskById(id)?.toDomain()
    }

    override suspend fun insertTask(task: Task) {
        try {
            remoteDataSource.createTask(task.toDto())
            localDataSource.insertTask(task.toEntity())
        } catch (e: Exception) {
            // If remote fails, insert only locally
            localDataSource.insertTask(task.toEntity())
            throw e
        }
    }

    override suspend fun updateTask(task: Task) {
        try {
            // First try to update remotely
            remoteDataSource.updateTask(task.id ?: throw Exception("Task ID is null"), task.toDto())
            // Then update locally
            localDataSource.updateTask(task.toEntity())
        } catch (e: Exception) {
            // If remote fails, update only locally
            localDataSource.updateTask(task.toEntity())
            throw e
        }
    }

    override suspend fun deleteTask(task: Task) {
        try {
            // First try to delete remotely
            remoteDataSource.deleteTask(task.id ?: throw Exception("Task ID is null"))
            // Then delete locally
            localDataSource.deleteTask(task.toEntity())
        } catch (e: Exception) {
            // If remote fails, delete only locally
            localDataSource.deleteTask(task.toEntity())
            throw e
        }
    }
}