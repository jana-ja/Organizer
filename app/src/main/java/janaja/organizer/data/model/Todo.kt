package janaja.organizer.data.model

import com.noodle.Id
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.random.Random

class Todo(
    @Id // noodle sets id automatically
    var id: Long = 0,
    var title: String = "",
    // TODO richtige ID
    var body: MutableList<TodoLine> = mutableListOf(TodoLine(Random.nextLong(), "")),
    var timePeriod: Int? = null,
    var x: Int = 0, // reset every x days/weeks/months
    var y: Int = 0, // reset on y day of week/month
    var hour: Int = 0,
    private var lastResetTimeString: String = "" // DD.MM.YYYY.hh
) {

    // TODO testing and exception handling

    fun tryReset(): Boolean{
        return if(lastResetTimeString.length != 13){
            false
        } else {
            // parse time
            val lastResetTime: LocalDateTime = parseDateTimeFromString(lastResetTimeString)

            if (isResetTime(lastResetTime))
                reset(lastResetTime)
            else
                false
        }
    }

    private fun reset(lastResetTime: LocalDateTime) : Boolean{
        val now: LocalDateTime = LocalDateTime.now()
        var newLastResetTime: LocalDateTime = lastResetTime
        // reset time
        when(timePeriod){
            // go forward in step size x until overstepping now
            // then go back one step
            TimePeriod.DAYS -> {
                while(newLastResetTime.isBefore(now))
                    newLastResetTime = lastResetTime.plusDays(x.toLong())
                newLastResetTime = lastResetTime.minusDays(x.toLong())
            }
            TimePeriod.WEEKS -> {
                while(newLastResetTime.isBefore(now))
                    newLastResetTime = lastResetTime.plusWeeks(x.toLong())
                newLastResetTime = lastResetTime.minusWeeks(x.toLong())
            }
            TimePeriod.MONTHS -> {
                while(newLastResetTime.isBefore(now))
                    newLastResetTime = lastResetTime.plusMonths(x.toLong())
                newLastResetTime = lastResetTime.minusMonths(x.toLong())
            }
            else -> return false
        }
        this.lastResetTimeString = parseStringFromDateTime(newLastResetTime)

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

    private fun isResetTime(lastResetTime: LocalDateTime): Boolean {
        // go back x days/weeks/months, if the time is after the time of the last reset then do a reset


        val now: LocalDateTime = LocalDateTime.now()
        var latestPossibleResetTime : LocalDateTime = now
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

    fun initResetTime(timePeriod: Int?, x: Int, y: Int, hour: Int){
        this.timePeriod = timePeriod
        this.x = x
        this.y = y
        this.hour = hour

        val now: LocalDateTime = LocalDateTime.now()
        var lastResetTime: LocalDateTime
        when(timePeriod){
            TimePeriod.DAYS -> {
                lastResetTime = now.withHour(hour).withMinute(0).withSecond(0)
                // if this lies in the future i need to go one day back for the next reset to happen correctly
                if(lastResetTime.isAfter(now)){
                    lastResetTime = lastResetTime.minusDays(1)
                }
                this.lastResetTimeString = parseStringFromDateTime(lastResetTime)
            }
            TimePeriod.WEEKS -> {
                lastResetTime = now.withHour(hour).withMinute(0).withSecond(0)
                // set weekday
                // DayOfWeek: monday = 1 ...
                val currentDayOfWeek = now.dayOfWeek.value
                val diff = abs(currentDayOfWeek - y)
                val backward = 7 - diff
                lastResetTime = lastResetTime.minusDays(backward.toLong())
                // if this lies in the future go back one week (happens when today is the right day of the week and reset hour has not passed yet)
                if(lastResetTime.isAfter(now)){
                    lastResetTime = lastResetTime.minusWeeks(1)
                }
                this.lastResetTimeString = parseStringFromDateTime(lastResetTime)

            }
            TimePeriod.MONTHS -> {
                lastResetTime = now.withHour(hour).withMinute(0).withSecond(0).withDayOfMonth(y)
                // if this lies in the future go back one month
                if(lastResetTime.isAfter(now)){
                    lastResetTime = lastResetTime.minusMonths(1)
                }
                this.lastResetTimeString = parseStringFromDateTime(lastResetTime)

            }
            null -> {
                this.lastResetTimeString = ""
            }

        }

    }

    private fun parseDateTimeFromString(dateTimeString: String): LocalDateTime{
        val times = dateTimeString.split(".")
        val day = times[0].toInt()
        val month = times[1].toInt()
        val year = times[2].toInt()
        val hour = times[3].toInt()
        return LocalDateTime.now().withDayOfMonth(day).withMonth(month).withYear(year).withHour(hour).withMinute(0).withSecond(0)
    }

    private fun parseStringFromDateTime(dateTime: LocalDateTime): String{
        return "${dateTime.dayOfMonth}.${dateTime.monthValue}.${dateTime.year}.${dateTime.hour}"
    }
}