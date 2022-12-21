package janaja.organizer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomTodo(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String,
    var timePeriod: Int?,
    var x: Int = 0, // reset every x days/weeks/months
    var y: Int = 0, // reset on y day of week/month
    var hour: Int = 0,
    var lastResetTimeString: String = "" // DD.MM.YYYY.hh
){
    fun toTodo(body: MutableList<TodoLine>): Todo{
        return Todo(id,title, body, timePeriod, x, y, hour, lastResetTimeString)
    }
}