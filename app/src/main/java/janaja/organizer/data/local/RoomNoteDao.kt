package janaja.organizer.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import janaja.organizer.data.model.RoomNote

@Dao
interface RoomNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<RoomNote>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: RoomNote)

    @Update
    suspend fun update(note: RoomNote)

    @Query("SELECT * FROM RoomNote")
    fun getAllLiveData() : LiveData<List<RoomNote>>

    @Query("SELECT * FROM RoomNote")
    fun getAll() : List<RoomNote>

    @Query("SELECT * FROM RoomNote WHERE id = :id")
    suspend fun getById(id: Long) : RoomNote

    @Query("DELETE FROM RoomNote")
    suspend fun deleteAll()

    @Query("DELETE FROM RoomNote WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM RoomNote) THEN 0 ELSE 1 END")
    suspend fun isEmpty(): Boolean
}