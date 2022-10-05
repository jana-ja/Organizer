package janaja.organizer.data.model

data class Note(
    val id: Long,
    val title: String,
    val body: String,
    val isCheckList: Boolean = false,
    val categories: MutableList<Category> = mutableListOf()
) {
}