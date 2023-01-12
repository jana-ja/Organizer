package janaja.organizer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomNote(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var title: String = "",
    var isCheckList: Boolean = false
) {
    fun toNote(body: MutableList<NoteLine>, categories: MutableList<Category>): Note {
        return Note(id, title, body, isCheckList, categories)
    }
}