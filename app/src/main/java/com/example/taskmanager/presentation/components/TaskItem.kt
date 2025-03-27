package com.example.taskmanager.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.domain.model.Task

@Composable
fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onCompleteChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onTaskClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCompleteChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    MaterialTheme {
        TaskItem(
            task = Task(
                id = 1,
                title = "Task 1",
                description = "Task 1 not completed",
                isCompleted = false
            ),
            onTaskClick = {},
            onCompleteChange = {}
        )
    }
}