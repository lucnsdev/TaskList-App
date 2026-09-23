package lucns.tasklist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import lucns.tasklist.models.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()
        fun getDatabase(context: Context): AppDatabase {
            if (instance == null)
                synchronized(LOCK) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "my_custom_task_database"
                    ).allowMainThreadQueries().build()
                }
            return instance!!
        }
    }
}