package janaja.organizer.data.model

class TodoLine(
    var text: String,
    var isChecked: Boolean = false,
    var repeat: Boolean = false,
    var indentationLevel: Int = 0
) {
    companion object {
        const val maxIndent: Int = 2
    }

    override fun toString(): String {
        return text
    }

    fun equalContent(line: TodoLine): Boolean {
        if (repeat != line.repeat || text != line.text || isChecked != line.isChecked || indentationLevel != line.indentationLevel)
            return false
        return true
    }

    fun copyLine(): TodoLine {
        return TodoLine(text, isChecked, repeat, indentationLevel)
    }

    fun toRoomTodoLine(todoId: Long): RoomTodoLine {
        return RoomTodoLine(text, isChecked, repeat, todoId, indentationLevel)
    }
}