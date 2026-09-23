package lucns.tasklist.repositories

import kotlinx.coroutines.flow.Flow
import lucns.tasklist.models.Task;

interface TaskRepository {

    fun getAllItems(): Flow<List<Task>>

    fun getItem(id: Int): Flow<Task?>

    suspend fun insertItem(task: Task)

    suspend fun deleteItem(task: Task)

    suspend fun updateItem(task: Task)
}
