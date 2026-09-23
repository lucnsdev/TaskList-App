package lucns.tasklist.repositories

import kotlinx.coroutines.flow.Flow
import lucns.tasklist.database.TaskDao
import lucns.tasklist.models.Task

class OfflineItemsRepository(private val taskDao: TaskDao) : TaskRepository {
    override fun getAllItems(): Flow<List<Task>> = taskDao.getAllItems()

    override fun getItem(id: Int): Flow<Task?> = taskDao.getItem(id)

    override suspend fun insertItem(task: Task) = taskDao.insert(task)

    override suspend fun deleteItem(task: Task) = taskDao.delete(task)

    override suspend fun updateItem(task: Task) = taskDao.update(task)
}