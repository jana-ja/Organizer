package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import janaja.organizer.R
import janaja.organizer.adapter.NoteRecyclerViewAdapter
import janaja.organizer.databinding.HomeCardviewBinding
import janaja.organizer.databinding.NoteCardviewContentBinding
import janaja.organizer.databinding.TodoCardviewHeaderBinding

class TodoCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private val gurki = "beste babe"
    private val binding: HomeCardviewBinding
    private val headerBinding: TodoCardviewHeaderBinding
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
            R.layout.todo_cardview_header,
            binding.flHomeCardviewHeader,
            true
        )
        headerBinding.tvTodoCardviewTitle.text = resources.getString(R.string.reminder_cardview_title)

        // manage content
        contentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.note_cardview_content,
            binding.flHomeCardviewContent,
            true
        )
        LinearLayoutManager(context).also{
            it.orientation = LinearLayoutManager.HORIZONTAL
            contentBinding.rvHomeCardviewNotes.layoutManager = it
        }
    }


    fun setNoteRecyclerViewAdapter(adapter: NoteRecyclerViewAdapter) {
        contentBinding.rvHomeCardviewNotes.adapter = adapter
    }
}