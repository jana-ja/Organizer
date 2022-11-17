package janaja.organizer.data.model

import kotlin.random.Random

data class Note(
    val id: Long,
    var title: String = "",
    // TODO richtige ID
    var body: MutableList<Line> = mutableListOf(Line(Random.nextLong(),"",false)),
    var isCheckList: Boolean = false,
    var categories: MutableList<Category> = mutableListOf(),
    var isTodo: Boolean = false,
    var hasReminder: Boolean = false,
    val duration: Long? = null,
    val repeatTime: Long? = null
) {
}