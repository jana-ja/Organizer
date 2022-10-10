package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import janaja.organizer.R
import janaja.organizer.adapter.CategorySpinnerAdapter
import janaja.organizer.adapter.ReminderRecyclerViewAdapter
import janaja.organizer.data.Repository
import janaja.organizer.databinding.HomeCardviewBinding
import janaja.organizer.databinding.NoteCardviewHeaderBinding
import janaja.organizer.databinding.ReminderCardviewContentBinding

class ReminderCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private val gurki = "beste babe"
    private val binding: HomeCardviewBinding
    private val headerBinding: NoteCardviewHeaderBinding
    private val contentBinding: ReminderCardviewContentBinding

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
            R.layout.note_cardview_header,
            binding.flHomeCardviewHeader,
            true
        )
        headerBinding.tvNoteCardviewTitle.text = resources.getString(R.string.reminder_cardview_title)
        headerBinding.spNoteCardviewCategory.also {
            // TODO dummy content
            it.adapter = CategorySpinnerAdapter(context = context, itemList = listOf("Generell", "Backlog", "Kp"))
            it.dropDownHorizontalOffset = it.height
        }

        // manage content
        contentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.reminder_cardview_content,
            binding.flHomeCardviewContent,
            true
        )
        ReminderRecyclerViewAdapter().also {
            it.submitList(Repository.getInstance().dummyData)
            contentBinding.rvHomeCardviewNotes.adapter = it
        }

    }
}