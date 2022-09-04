package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import janaja.organizer.R
import janaja.organizer.databinding.HomeCardviewBinding

class NoteCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    var titleText: String = ""
    val gurki = "beste babe"
    val binding: HomeCardviewBinding

    init {
        //inflate(context, R.layout.note_cardview, this)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.home_cardview,
            this,
            true
        )
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

//
//        binding.rvHomeCardviewNotes.addItemDecoration(
//            DividerItemDecoration(
//                context,
//                LinearLayoutManager.VERTICAL
//            )
//        )

        // TODO ist framelayout und nicht spinner
        //val spinner: Spinner =
        //val frameLayout: FrameLayout = inflate(context, R.layout.note_cardview_header, binding.flHomeCardviewHeader) as FrameLayout
                    //as Spinner
        //frameLayout.children

        val header: NoteCardViewHeader = inflate(context, R.layout.note_cardview_header, null) as NoteCardViewHeader
        binding.flHomeCardviewHeader.addView(header)
        //val rv: RecyclerView =
        inflate(context, R.layout.note_cardview_content, binding.flHomeCardviewContent)
                //as RecyclerView
        //rv.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        // TODO recycler und spinner adapter

    }
}