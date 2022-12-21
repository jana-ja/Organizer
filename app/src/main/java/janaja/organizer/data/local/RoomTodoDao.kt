package janaja.organizer.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import janaja.organizer.data.model.RoomTodo

@Dao
interface RoomTodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todos: List<RoomTodo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: RoomTodo)

    @Update
    suspend fun update(todo: RoomTodo)

    @Query("SELECT * FROM RoomTodo")
    fun getAllLiveData() : LiveData<List<RoomTodo>>

    @Query("SELECT * FROM RoomTodo")
    fun getAll() : List<RoomTodo>

    @Query("SELECT * FROM RoomTodo WHERE id = :id")
    suspend fun getById(id: Long) : RoomTodo

    @Query("DELETE FROM RoomTodo")
    suspend fun deleteAll()

    @Query("DELETE FROM RoomTodo WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM RoomTodo) THEN 0 ELSE 1 END")
    suspend fun isEmpty(): Boolean
}