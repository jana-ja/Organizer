package janaja.organizer.data.model


class NoteLine(
    var text: String,
    var isChecked: Boolean = false
) {
    override fun toString(): String {
        return text
    }

    fun equalContent(line: NoteLine): Boolean {
        if(text != line.text || isChecked != line.isChecked)
            return false
        return true
    }

    fun copyLine(): NoteLine {
        return NoteLine(text,isChecked)
    }

    fun toRoomNoteLine(noteId: Long): RoomNoteLine{
        return RoomNoteLine(text, isChecked, noteId)
    }
}