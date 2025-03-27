package com.example.taskmanager.data.mapper

import com.example.taskmanager.data.local.entities.TaskEntity
import com.example.taskmanager.data.remote.dto.TaskDto
import com.example.taskmanager.domain.model.Task

fun TaskDto.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}

fun Task.toDto(): TaskDto {
    return TaskDto(
        id = id ?: 0,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}