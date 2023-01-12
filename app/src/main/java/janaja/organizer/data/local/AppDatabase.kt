package janaja.organizer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import janaja.organizer.data.model.RoomNote
import janaja.organizer.data.model.RoomNoteLine
import janaja.organizer.data.model.RoomTodo
import janaja.organizer.data.model.RoomTodoLine

@Database(entities = [RoomTodo::class, RoomTodoLine::class, RoomNote::class, RoomNoteLine::class], version = 1)
abstract class AppDatabase() : RoomDatabase() {

    abstract val roomTodoDao : RoomTodoDao
    abstract val roomTodoLineDao : RoomTodoLineDao
    abstract val roomNoteDao : RoomNoteDao
    abstract val roomNoteLineDao : RoomNoteLineDao
}

private lateinit var INSTANCE: AppDatabase

fun getDatabase(context: Context): AppDatabase {
    synchronized(AppDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "note_database"
            )
                .build()
        }
    }
    return INSTANCE
}

