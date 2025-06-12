package com.apptolast.lifetimejournal.models.repositories

import com.apptolast.lifetimejournal.models.Priority
import com.apptolast.lifetimejournal.models.Task

interface TaskRepository {
    fun allTasks(): List<Task>
    fun tasksByPriority(priority: Priority): List<Task>
    fun taskByName(name: String): Task?
    fun addTask(task: Task)
    fun removeTask(name: String): Boolean
}
