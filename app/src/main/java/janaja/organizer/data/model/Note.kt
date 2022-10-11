package janaja.organizer.data.model

data class Note(
    val id: Long,
    val title: String = "",
    val body: MutableList<Line> = mutableListOf(Line("",false)),
    val isCheckList: Boolean = false,
    val categories: MutableList<Category> = mutableListOf(),
    val isReminder: Boolean = false
//    val duration: Long,
//    val startDate: Date
) {
}