package janaja.organizer.data.model

import com.noodle.Id

class TodoLine(
    @Id
    var id: Long,
    var text: String,
    var isChecked: Boolean = false,
    var repeat: Boolean = false
) {
    override fun toString(): String {
        return text
    }

    fun equalContent(line: TodoLine): Boolean {
        if(repeat != line.repeat || text != line.text || isChecked != line.isChecked)
            return false
        return true
    }

    fun copyLine(): TodoLine {
        return TodoLine(id,text,isChecked,repeat)
    }
}