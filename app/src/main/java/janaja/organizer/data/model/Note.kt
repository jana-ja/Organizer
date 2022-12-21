package janaja.organizer.data.model

import com.noodle.Id
import kotlin.random.Random

data class Note(
    @Id
    var id: Long,
    var title: String = "",
    var body: MutableList<NoteLine> = mutableListOf(NoteLine(Random.nextLong(),"",false)),
    var isCheckList: Boolean = false,
    var categories: MutableList<Category> = mutableListOf()
)