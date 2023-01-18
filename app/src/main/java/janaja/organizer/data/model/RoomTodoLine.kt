package janaja.organizer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomTodoLine(
    var text: String,
    var isChecked: Boolean = false,
    var repeat: Boolean = false,
    var todoId: Long,
    var indentationLevel: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
) {
    fun toTodoLine(): TodoLine {
        return TodoLine(text, isChecked, repeat, indentationLevel)
    }
}