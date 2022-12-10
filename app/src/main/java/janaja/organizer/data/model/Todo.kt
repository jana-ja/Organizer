package janaja.organizer.data.model

import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.random.Random

data class Todo(
    val id: Long,
    var title: String = "",
    // TODO richtige ID
    var body: MutableList<Line> = mutableListOf(Line(Random.nextLong(), "", false)),
    var timePeriod: TimePeriod? = null,
    var x: Int = 0, // reset every x days/weeks/months
    var y: Int = 0, // reset on y day of week/month
    var hour: Int = 0,
    var lastResetTime: LocalDateTime? = null
) {
    // TODO testing and exception handling

    enum class TimePeriod { DAYS, WEEKS, MONTHS }

    fun tryReset(): Boolean{
        val resetTime = isResetTime()
        if(resetTime) reset()
        return resetTime
    }

    private fun reset() : Boolean{
        val now: LocalDateTime = LocalDateTime.now()
        // reset time
        when(timePeriod){
            // go forward in step size x until overstepping now
            // then go back one step
            TimePeriod.DAYS -> {
                while(lastResetTime!!.isBefore(now))
                    lastResetTime = lastResetTime!!.plusDays(x.toLong())
                lastResetTime = lastResetTime!!.minusDays(x.toLong())
            }
            TimePeriod.WEEKS -> {
                while(lastResetTime!!.isBefore(now))
                    lastResetTime = lastResetTime!!.plusWeeks(x.toLong())
                lastResetTime = lastResetTime!!.minusWeeks(x.toLong())
            }
            TimePeriod.MONTHS -> {
                while(lastResetTime!!.isBefore(now))
                    lastResetTime = lastResetTime!!.plusMonths(x.toLong())
                lastResetTime = lastResetTime!!.minusMonths(x.toLong())
            }
            else -> return false
        }
        // reset data
        for(i in body.lastIndex downTo 0){
            val line = body[i]
            if(line.repeat){
                line.isChecked = false
            } else {
                if(line.isChecked)
                    body.removeAt(i)
            }
        }
        return true
    }

    private fun isResetTime(): Boolean {
        // go back x days/weeks/months, if the time is after the time of the last reset then do a reset

        val currentTime: LocalDateTime = LocalDateTime.now()
        var latestPossibleResetTime : LocalDateTime = currentTime
        when(timePeriod){
            TimePeriod.DAYS -> {
                latestPossibleResetTime = latestPossibleResetTime.minusDays(x.toLong())
            }
            TimePeriod.WEEKS -> {
                latestPossibleResetTime = latestPossibleResetTime.minusWeeks(x.toLong())

            }
            TimePeriod.MONTHS -> {
                latestPossibleResetTime = latestPossibleResetTime.minusMonths(x.toLong())

            }
            else -> return false
        }
        return latestPossibleResetTime.isAfter(lastResetTime)
    }

    fun initResetTime(timePeriod: TimePeriod?, x: Int, y: Int, hour: Int){
        this.timePeriod = timePeriod
        this.x = x
        this.y = y
        this.hour = hour

        val now: LocalDateTime = LocalDateTime.now()
        when(timePeriod){
            TimePeriod.DAYS -> {
                lastResetTime = now.withHour(hour).withMinute(0).withSecond(0)
                // if this lies in the future i need to go one day back for the next reset to happen correctly
                if(lastResetTime!!.isAfter(now)){
                    lastResetTime = lastResetTime!!.minusDays(1)
                }
            }
            TimePeriod.WEEKS -> {
                lastResetTime = now.withHour(hour).withMinute(0).withSecond(0)
                // set weekday
                // DayOfWeek: monday = 1 ...
                val currentDayOfWeek = now.dayOfWeek.value
                val diff = abs(currentDayOfWeek - y)
                val backward = 7 - diff
                lastResetTime = lastResetTime!!.minusDays(backward.toLong())
                // if this lies in the future go back one week (happens when today is the right day of the week and reset hour has not passed yet)
                if(lastResetTime!!.isAfter(now)){
                    lastResetTime = lastResetTime!!.minusWeeks(1)
                }
            }
            TimePeriod.MONTHS -> {
                lastResetTime = now.withHour(hour).withMinute(0).withSecond(0).withDayOfMonth(y)
                // if this lies in the future go back one month
                if(lastResetTime!!.isAfter(now)){
                    lastResetTime = lastResetTime!!.minusMonths(1)
                }
            }
            null -> {}

        }

    }
}