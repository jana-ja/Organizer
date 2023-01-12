package janaja.organizer.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import janaja.organizer.data.model.RoomNoteLine

@Dao
interface RoomNoteLineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(noteLines: List<RoomNoteLine>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOnlyNew(noteLines: List<RoomNoteLine>)

    @Update
    suspend fun update(noteLine: RoomNoteLine)

    @Query("SELECT * FROM RoomNoteLine")
    fun getAll() : LiveData<List<RoomNoteLine>>

    @Query("SELECT * FROM RoomNoteLine WHERE id = :id")
    suspend fun getById(id: Long) : RoomNoteLine

    @Query("SELECT * FROM RoomNoteLine WHERE noteId = :noteId")
    suspend fun getAllForNoteId(noteId: Long) : MutableList<RoomNoteLine>

    @Query("DELETE FROM RoomNoteLine")
    suspend fun deleteAll()

    @Query("DELETE FROM RoomNoteLine WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM RoomNoteLine WHERE noteId = :noteId")
    suspend fun deleteAllByNoteId(noteId: Long)

    @Update
    suspend fun updateAll(noteLines: List<RoomNoteLine>)

}