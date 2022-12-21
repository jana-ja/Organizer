package janaja.organizer.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import janaja.organizer.data.model.RoomTodoLine

@Dao
interface RoomTodoLineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todoLines: List<RoomTodoLine>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOnlyNew(todoLines: List<RoomTodoLine>)

    @Update
    suspend fun update(todoLine: RoomTodoLine)

    @Query("SELECT * FROM RoomTodoLine")
    fun getAll() : LiveData<List<RoomTodoLine>>

    @Query("SELECT * FROM RoomTodoLine WHERE id = :id")
    suspend fun getById(id: Long) : RoomTodoLine

    @Query("SELECT * FROM RoomTodoLine WHERE todoId = :todoId")
    suspend fun getAllForTodoId(todoId: Long) : MutableList<RoomTodoLine>

    @Query("DELETE FROM RoomTodoLine")
    suspend fun deleteAll()

    @Query("DELETE FROM RoomTodoLine WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM RoomTodoLine WHERE todoId = :todoId")
    suspend fun deleteAllByTodoId(todoId: Long)

    @Update
    suspend fun updateAll(todoLines: List<RoomTodoLine>)

    
}