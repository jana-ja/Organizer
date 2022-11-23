package janaja.organizer.data.model

import kotlin.random.Random

data class Todo (
    val id: Long,
    var title: String = "",
    // TODO richtige ID
    var body: MutableList<Line> = mutableListOf(Line(Random.nextLong(),"",false)),
    val duration: Long? = null,
    val lastResetTime: Long? = null
        ) {

}