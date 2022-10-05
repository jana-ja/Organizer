package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import janaja.organizer.R
import janaja.organizer.adapter.CategorySpinnerAdapter
import janaja.organizer.adapter.NoteRecyclerViewAdapter
import janaja.organizer.data.Repository
import janaja.organizer.data.model.Note
import janaja.organizer.databinding.HomeCardviewBinding
import janaja.organizer.databinding.NoteCardviewContentBinding
import janaja.organizer.databinding.NoteCardviewHeaderBinding

class NoteCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private val gurki = "beste babe"
    private val binding: HomeCardviewBinding
    private val headerBinding: NoteCardviewHeaderBinding
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
            R.layout.note_cardview_header,
            binding.flHomeCardviewHeader,
            true
        )
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
//        contentBinding.rvHomeCardviewNotes.addItemDecoration(
//            DividerItemDecoration(
//                context,
//                LinearLayoutManager.VERTICAL
//            )
//        )
        NoteRecyclerViewAdapter().also {
            it.submitList(Repository.getInstance().dummyData)
            contentBinding.rvHomeCardviewNotes.adapter = it
        }

    }
}