package janaja.organizer.data.model

class Line(
    var id: Long,
    var text: String,
    var isChecked: Boolean = false,
    var repeat: Boolean = false,
    var notify: Boolean = false // TODO this is shit
) {
    override fun toString(): String {
        return text
    }

    fun equalContent(line: Line): Boolean {
        if(notify != line.notify || repeat != line.repeat || text != line.text || isChecked != line.isChecked)
            return false
        return true
    }

    fun copyLine(): Line {
        return Line(id,text,isChecked,repeat,notify)
    }
}