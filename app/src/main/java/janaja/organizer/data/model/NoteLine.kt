package janaja.organizer.data.model

import com.noodle.Id

class NoteLine(
    @Id
    var id: Long,
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
        return NoteLine(id,text,isChecked)
    }
}