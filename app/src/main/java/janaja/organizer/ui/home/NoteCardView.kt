package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import janaja.organizer.R
import janaja.organizer.databinding.DecisionCardviewHeaderBinding
import janaja.organizer.databinding.HomeCardviewBinding
import janaja.organizer.databinding.NoteCardviewContentBinding
import janaja.organizer.databinding.NoteCardviewHeaderBinding

class NoteCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    val gurki = "beste babe"
    val binding: HomeCardviewBinding
    val headerBinding: NoteCardviewHeaderBinding
    val contentBinding: NoteCardviewContentBinding

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


        // manage content
        contentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.note_cardview_content,
            binding.flHomeCardviewContent,
            true
        )
        contentBinding.rvHomeCardviewNotes.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }
}