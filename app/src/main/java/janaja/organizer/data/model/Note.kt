package janaja.organizer.data.model

data class Note(
    val id: Long,
    val title: String,
    val body: MutableList<String>,
    val isCheckList: Boolean = false,
    val categories: MutableList<Category> = mutableListOf(),
    val isReminder: Boolean = false
//    val duration: Long,
//    val startDate: Date
) {
}