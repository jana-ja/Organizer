package janaja.organizer.data.model

data class Note(
    var id: Long = 0,
    var title: String = "",
    var body: MutableList<NoteLine> = mutableListOf(NoteLine("", false)),
    var isCheckList: Boolean = false,
    var categories: MutableList<Category> = mutableListOf()
) {
    fun toRoomNote(): RoomNote {
        return RoomNote(id, title, isCheckList)
    }
}