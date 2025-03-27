package com.example.taskmanager.data.remote.api

import com.example.taskmanager.data.remote.dto.TaskDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {
    @GET("tasks")
    suspend fun getTasks(): List<TaskDto>

    @GET("tasks/{id}")
    suspend fun getTaskById(@Path("id") id: Int): TaskDto

    @POST("tasks")
    suspend fun createTask(@Body task: TaskDto): TaskDto

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body task: TaskDto): TaskDto

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int)
}