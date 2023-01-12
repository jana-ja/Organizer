package janaja.organizer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomNoteLine(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var text: String,
    var isChecked: Boolean = false,
    var noteId: Long
) {
    fun toNoteLine(): NoteLine{
        return NoteLine(text, isChecked)
    }
}