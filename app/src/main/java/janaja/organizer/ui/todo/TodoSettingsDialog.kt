package janaja.organizer.ui.todo

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import janaja.organizer.R
import janaja.organizer.data.model.TimePeriod
import janaja.organizer.data.model.Todo
import janaja.organizer.databinding.DialogTodoSettingsBinding

class TodoSettingsDialog(val todo: Todo) : DialogFragment() {

    private lateinit var binding: DialogTodoSettingsBinding

    private var selected: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogTodoSettingsBinding.inflate(requireActivity().layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
            .setTitle(R.string.todo_settings_title)
        val dialog = builder.create()

        // dialog buttons
        binding.btnPos.setOnClickListener {
            if (saveWithInput())
                dialog.dismiss()
        }
        binding.btnNeg.setOnClickListener {
            dialog.cancel()

        }
        // checkbox radio group
        binding.cbDays.setOnClickListener {
            if (!binding.cbDays.isChecked) {
                deactivate(TimePeriod.DAYS)
                selected = null
            } else {
                activate(TimePeriod.DAYS)
                deactivate(TimePeriod.WEEKS)
                deactivate(TimePeriod.MONTHS)
            }
        }
        binding.cbWeeks.setOnClickListener {
            if (!binding.cbWeeks.isChecked) {
                deactivate(TimePeriod.WEEKS)
                selected = null
            } else {
                deactivate(TimePeriod.DAYS)
                activate(TimePeriod.WEEKS)
                deactivate(TimePeriod.MONTHS)
            }
        }
        binding.cbMonths.setOnClickListener {
            if (!binding.cbMonths.isChecked) {
                deactivate(TimePeriod.MONTHS)
                selected = null
            } else {
                deactivate(TimePeriod.DAYS)
                deactivate(TimePeriod.WEEKS)
                activate(TimePeriod.MONTHS)
            }
        }

        // view body
        when (todo.timePeriod) {
            TimePeriod.DAYS -> {
                binding.cbDays.isChecked = true
                binding.cbDays.callOnClick()
                binding.etXDays.setText(todo.x.toString())
                binding.etTimeDays.setText(todo.hour.toString())
            }
            TimePeriod.WEEKS -> {
                binding.cbWeeks.isChecked = true
                binding.cbWeeks.callOnClick()
                binding.etXWeeks.setText(todo.x.toString())
                binding.etOnWeeks.setText(todo.y.toString())
                binding.etTimeWeeks.setText(todo.hour.toString())
            }
            TimePeriod.MONTHS -> {
                binding.cbMonths.isChecked = true
                binding.cbMonths.callOnClick()
                binding.etXMonths.setText(todo.x.toString())
                binding.etOnMonths.setText(todo.y.toString())
                binding.etTimeMonths.setText(todo.hour.toString())
            }
            null -> {}
        }

        return dialog
    }

    private fun saveWithInput(): Boolean {
        val x: Int?
        val y: Int?
        val hour: Int?

        when (selected) {
            TimePeriod.DAYS -> {
                x = parseEditableToInt(binding.etXDays.text)
                hour = parseHour(binding.etTimeDays.text)
                if (x == null || hour == null)
                    return false
                y = 0
            }
            TimePeriod.WEEKS -> {
                x = parseEditableToInt(binding.etXWeeks.text)
                y = parseDayInWeek(binding.etOnWeeks.text)
                hour = parseHour(binding.etTimeWeeks.text)
                if (x == null || y == null || hour == null)
                    return false
            }
            TimePeriod.MONTHS -> {
                x = parseEditableToInt(binding.etXMonths.text)
                y = parseDayInMonth(binding.etOnMonths.text)
                hour = parseHour(binding.etTimeMonths.text)
                if (x == null || y == null || hour == null)
                    return false
            }
            else -> {
                x = 0
                y = 0
                hour = 0
            }
        }
        todo.initResetTime(selected, x, y, hour)
        return true
    }

    private fun parseEditableToInt(input: Editable): Int? {
        return try {
            kotlin.math.abs(input.toString().toInt())
        } catch (e: java.lang.NumberFormatException) {
            Toast.makeText(requireContext(), "Only Numbers accepted as Input", Toast.LENGTH_SHORT).show()
            null
        }

    }

    private fun parseDayInWeek(input: Editable): Int? {
        val day = parseEditableToInt(input)
        if (day != null) {
            if ((day < 1) || (day > 7)) {
                Toast.makeText(requireContext(), "Only days between 1 and 7 are accepted", Toast.LENGTH_SHORT).show()
                return null
            }
        }
        return day
    }

    private fun parseDayInMonth(input: Editable): Int? {
        val day = parseEditableToInt(input)
        if (day != null) {
            if ((day < 1) || (day > 28)) {
                Toast.makeText(requireContext(), "Only days between 1 and 28 are accepted", Toast.LENGTH_SHORT).show()
                return null
            }
        }
        return day
    }

    private fun parseHour(input: Editable): Int? {
        val hour = parseEditableToInt(input)
        if (hour != null) {
            if ((hour < 0) || (hour > 23)) {
                Toast.makeText(requireContext(), "Only hours between 0 and 23 are accepted", Toast.LENGTH_SHORT).show()
                return null
            }
        }
        return hour
    }


    // TDOD alle deactivate timeperdio auf null setzen

    private fun activate(timePeriod: Int) {
        selected = timePeriod
        when (timePeriod) {
            TimePeriod.DAYS -> {
                binding.etXDays.isEnabled = true
                binding.etTimeDays.isEnabled = true
            }
            TimePeriod.WEEKS -> {
                binding.etXWeeks.isEnabled = true
                binding.etTimeWeeks.isEnabled = true
            }
            TimePeriod.MONTHS -> {
                binding.etXMonths.isEnabled = true
                binding.etTimeMonths.isEnabled = true
            }
        }
    }

    private fun deactivate(timePeriod: Int) {
        when (timePeriod) {
            TimePeriod.DAYS -> {
                binding.cbDays.isChecked = false
                binding.etXDays.isEnabled = false
                binding.etTimeDays.isEnabled = false
            }
            TimePeriod.WEEKS -> {
                binding.cbWeeks.isChecked = false
                binding.etXWeeks.isEnabled = false
                binding.etTimeWeeks.isEnabled = false
            }
            TimePeriod.MONTHS -> {
                binding.cbMonths.isChecked = false
                binding.etXMonths.isEnabled = false
                binding.etTimeMonths.isEnabled = false
            }
        }
    }

}