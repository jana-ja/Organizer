package janaja.organizer.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import janaja.organizer.R
import janaja.organizer.databinding.NoteCardviewBinding

class NoteCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    var titleText: String = ""
    val gurki = "beste babe"
    val binding: NoteCardviewBinding

    init {
        //inflate(context, R.layout.note_cardview, this)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.note_cardview, this, true)
      //binding = NoteCardviewBinding.inflate(LayoutInflater.from(context))

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyCardView,
            0, 0
        ).apply {

            try {
                titleText = getString(R.styleable.MyCardView_title_text).toString()
            } finally {
                recycle()
            }
        }


       binding.rvNoteCardviewNotes.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        // TODO recycler und spinner adapter

    }
}