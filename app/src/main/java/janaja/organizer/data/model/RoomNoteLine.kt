package janaja.organizer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomNoteLine(
    var text: String,
    var isChecked: Boolean = false,
    var noteId: Long,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) {
    fun toNoteLine(): NoteLine {
        return NoteLine(text, isChecked)
    }
}