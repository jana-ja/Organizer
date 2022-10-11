package janaja.organizer.data.model

class Line(
    var text: String,
    var isChecked: Boolean = false
) {
    override fun toString(): String {
        return text
    }
}