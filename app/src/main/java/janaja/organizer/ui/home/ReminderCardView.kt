package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import janaja.organizer.R
import janaja.organizer.adapter.ReminderRecyclerViewAdapter
import janaja.organizer.data.Repository
import janaja.organizer.databinding.HomeCardviewBinding
import janaja.organizer.databinding.NoteCardviewContentBinding
import janaja.organizer.databinding.ReminderCardviewHeaderBinding

class ReminderCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private val gurki = "beste babe"
    private val binding: HomeCardviewBinding
    private val headerBinding: ReminderCardviewHeaderBinding
    private val contentBinding: NoteCardviewContentBinding

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.home_cardview,
            this,
            true
        )

        // manage header
        headerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.reminder_cardview_header,
            binding.flHomeCardviewHeader,
            true
        )
        headerBinding.tvReminderCardviewTitle.text = resources.getString(R.string.reminder_cardview_title)

        // manage content
        contentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.note_cardview_content,
            binding.flHomeCardviewContent,
            true
        )
        ReminderRecyclerViewAdapter().also {
            it.submitList(Repository.getInstance().dummyTodoData)
            contentBinding.rvHomeCardviewNotes.adapter = it
        }
        LinearLayoutManager(context).also{
            it.orientation = LinearLayoutManager.HORIZONTAL
            contentBinding.rvHomeCardviewNotes.layoutManager = it
        }
    }
}