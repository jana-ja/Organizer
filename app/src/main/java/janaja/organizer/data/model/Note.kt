package janaja.organizer.data.model

data class Note(
    val id: Long,
    var title: String = "",
    var body: MutableList<Line> = mutableListOf(Line("",false)),
    var isCheckList: Boolean = false,
    var categories: MutableList<Category> = mutableListOf(),
    var isReminder: Boolean = false
//    val duration: Long,
//    val startDate: Date
) {
}