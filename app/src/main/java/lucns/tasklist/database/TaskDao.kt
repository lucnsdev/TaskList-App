package lucns.tasklist.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import lucns.tasklist.models.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Task>

    @Query("SELECT * from items ORDER BY id ASC")
    fun getAllItems(): Flow<List<Task>>
}