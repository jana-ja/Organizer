package janaja.organizer.data.model


class NoteLine(
    var text: String,
    var isChecked: Boolean = false
) {
    override fun toString(): String {
        return text
    }

    fun copyLine(): NoteLine {
        return NoteLine(text,isChecked)
    }

    fun toRoomNoteLine(noteId: Long): RoomNoteLine{
        return RoomNoteLine(text, isChecked, noteId)
    }

    override fun equals(other: Any?): Boolean {
        if(other !is  NoteLine)
            return false
        if(text != other.text || isChecked != other.isChecked)
            return false
        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + isChecked.hashCode()
        return result
    }
}