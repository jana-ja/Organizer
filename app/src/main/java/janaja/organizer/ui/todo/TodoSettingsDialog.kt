package janaja.organizer.ui.todo

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import janaja.organizer.R
import janaja.organizer.data.model.Todo
import janaja.organizer.data.model.Todo.TimePeriod
import janaja.organizer.databinding.DialogTodoSettingsBinding

class TodoSettingsDialog(val todo: Todo) : DialogFragment() {

    private lateinit var binding: DialogTodoSettingsBinding

    private var selected: TimePeriod? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())

        binding = DialogTodoSettingsBinding.inflate(requireActivity().layoutInflater)

        // dialog buttons
        builder.setView(binding.root)
            .setPositiveButton(R.string.apply_button) { dialog, id ->
                saveWithInput()
            }
            .setNegativeButton(R.string.cancel_button) { dialog, id ->
                dialog.cancel()
            }
            .setTitle(R.string.todo_settings_title)

        // view body
        when (todo.timePeriod) {
            TimePeriod.DAYS -> {
                binding.cbDays.callOnClick()
                binding.etXDays.setText(todo.x.toString())
                binding.etTimeDays.setText(todo.hour.toString())
            }
            TimePeriod.WEEKS -> {
                binding.cbWeeks.callOnClick()
                binding.etXWeeks.setText(todo.x.toString())
                binding.etOnWeeks.setText(todo.y.toString())
                binding.etTimeWeeks.setText(todo.hour.toString())
            }
            TimePeriod.MONTHS -> {
                binding.cbMonths.callOnClick()
                binding.etXMonths.setText(todo.x.toString())
                binding.etOnMonths.setText(todo.y.toString())
                binding.etTimeMonths.setText(todo.hour.toString())
            }
            null -> {}
        }

        // checkbox radio group
        binding.cbDays.setOnClickListener {
            activate(TimePeriod.DAYS)
            deactivate(TimePeriod.WEEKS)
            deactivate(TimePeriod.MONTHS)
        }
        binding.cbWeeks.setOnClickListener {
            deactivate(TimePeriod.DAYS)
            activate(TimePeriod.WEEKS)
            deactivate(TimePeriod.MONTHS)
        }
        binding.cbMonths.setOnClickListener {
            deactivate(TimePeriod.DAYS)
            deactivate(TimePeriod.WEEKS)
            activate(TimePeriod.MONTHS)
        }


        return builder.create()
    }

    private fun saveWithInput() {
        val x: Int
        val y: Int
        val hour: Int

        when (selected) {
            TimePeriod.DAYS -> {
                // TODO check input
                x = binding.etXDays.text.toString().toInt()
                hour = binding.etTimeDays.toString().toInt()
                todo.initResetTime(selected, x, hour = hour)

            }
            TimePeriod.WEEKS -> {
                x = binding.etXWeeks.text.toString().toInt()
                y = binding.etOnWeeks.text.toString().toInt()
                hour = binding.etTimeWeeks.toString().toInt()
                todo.initResetTime(selected, x, y, hour)

            }
            TimePeriod.MONTHS -> {
                x = binding.etXMonths.text.toString().toInt()
                y = binding.etOnMonths.text.toString().toInt()
                hour = binding.etTimeMonths.toString().toInt()
                todo.initResetTime(selected, x, y, hour)

            }
            else -> todo.initResetTime(selected)
        }
    }

    // TDOD alle deactivate timeperdio auf null setzen

    private fun activate(timePeriod: TimePeriod) {
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

    private fun deactivate(timePeriod: TimePeriod) {
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