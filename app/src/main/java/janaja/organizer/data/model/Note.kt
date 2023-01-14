package janaja.organizer.data.model

data class Note(
    var id: Long,
    var title: String = "",
    var body: MutableList<NoteLine> = mutableListOf(NoteLine("", false)),
    var isCheckList: Boolean = false,
    var categories: MutableList<Category> = mutableListOf()
) {
    fun toRoomNote(): RoomNote {
        return RoomNote(id, title, isCheckList)
    }

    override fun equals(other: Any?): Boolean {
        if(other !is  Note)
            return false
        // check basics
        if(isCheckList != other.isCheckList || title != other.title || body.size != other.body.size || categories.size != other.categories.size)
            return false
        // check body
        for(i in body.indices){
            if(body[i] != other.body[i])
                return false
        }
        // check categories
        for(i in categories.indices){
            if(categories[i] != other.categories[i])
                return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + isCheckList.hashCode()
        result = 31 * result + categories.hashCode()
        return result
    }
}