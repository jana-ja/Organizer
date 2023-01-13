package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import janaja.organizer.R
import janaja.organizer.adapter.CategorySpinnerAdapter
import janaja.organizer.adapter.HomeNoteRVA
import janaja.organizer.data.model.Note
import janaja.organizer.databinding.HomeCardviewBinding
import janaja.organizer.databinding.NoteCardviewContentBinding
import janaja.organizer.databinding.NoteCardviewHeaderBinding

class NoteCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private val gurki = "beste babe"
    private val binding: HomeCardviewBinding
    private val headerBinding: NoteCardviewHeaderBinding
    private val contentBinding: NoteCardviewContentBinding
    private lateinit var adapter: HomeNoteRVA

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.home_cardview,
            this,
            true
        )

        // manage header
        headerBinding = DataBindingUtil.inflate<NoteCardviewHeaderBinding?>(
            LayoutInflater.from(context),
            R.layout.note_cardview_header,
            binding.flHomeCardviewHeader,
            true
        )
        headerBinding.tvNoteCardviewTitle.text = resources.getString(R.string.note_cardview_title)
        headerBinding.spNoteCardviewCategory.also {
            // TODO dummy content
            it.adapter = CategorySpinnerAdapter(context = context, itemList = listOf("Wichtig", "Kaufen", "Traumtagebuch", "Erinnerungen"))
            it.dropDownHorizontalOffset = it.height
        }


        // manage content
        contentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.note_cardview_content,
            binding.flHomeCardviewContent,
            true
        )

    }

    fun updateNoteRecyclerViewAdapter(notes: MutableList<Note>){
        adapter.updateList(notes)
    }

    fun setNoteRecyclerViewAdapter(adapter: HomeNoteRVA) {
        this.adapter = adapter
        contentBinding.rvHomeCardviewNotes.adapter = adapter
    }

    fun setAddbuttonOnClickListener(listener: OnClickListener){
        // TODO manage contents of NoteCardView like this or better way?
        headerBinding.cardviewAddButton.setOnClickListener(listener)
    }

}