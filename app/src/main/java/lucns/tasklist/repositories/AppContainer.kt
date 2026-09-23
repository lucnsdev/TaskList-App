package lucns.tasklist.repositories

import android.content.Context
import lucns.tasklist.database.AppDatabase

interface AppContainer {
    val taskRepository: TaskRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val taskRepository: TaskRepository by lazy {
        OfflineItemsRepository(AppDatabase.getDatabase(context).taskDao())
    }
}