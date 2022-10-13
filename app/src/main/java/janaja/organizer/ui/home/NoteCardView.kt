package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import janaja.organizer.R
import janaja.organizer.adapter.CategorySpinnerAdapter
import janaja.organizer.adapter.NoteRecyclerViewAdapter
import janaja.organizer.data.model.Note
import janaja.organizer.databinding.HomeCardviewBinding
import janaja.organizer.databinding.NoteCardviewContentBinding
import janaja.organizer.databinding.NoteCardviewHeaderBinding
import kotlin.random.Random

class NoteCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private val gurki = "beste babe"
    private val binding: HomeCardviewBinding
    private val headerBinding: NoteCardviewHeaderBinding
    private val contentBinding: NoteCardviewContentBinding
    private lateinit var adapter: NoteRecyclerViewAdapter

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
        headerBinding.cardviewAddButton.setOnClickListener{
            // TODO dummy data id
            val id = Random.nextLong()
            adapter.addItem(Note(id))
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment(id))
        }

        // manage content
        contentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.note_cardview_content,
            binding.flHomeCardviewContent,
            true
        )
    }

    fun setNoteRecyclerViewAdapter(adapter: NoteRecyclerViewAdapter) {
        contentBinding.rvHomeCardviewNotes.adapter = adapter
    }

}