package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import janaja.organizer.R
import janaja.organizer.databinding.HomeCardviewBinding

class DecisionCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    var titleText: String = ""
    val gurki = "beste babe"
    val binding: HomeCardviewBinding

    init {
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


//        val header: NoteCardViewHeader = inflate(context, R.layout.decision_cardview_header, null) as NoteCardViewHeader
//        binding.flHomeCardviewHeader.addView(header)
        binding.flHomeCardviewHeader.addView(inflate(context, R.layout.decision_cardview_header, null))

//        val content: DecisionCardViewContent = inflate(context, R.layout.decision_cardview_content, null) as DecisionCardViewContent
//        binding.flHomeCardviewContent.addView(content)
        val params = binding.flHomeCardviewContent.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.flHomeCardviewContent.layoutParams = params
        binding.flHomeCardviewContent.addView(inflate(context, R.layout.decision_cardview_content, null))

        findViewById<TextView>(R.id.textView3).text = "lol"
    }
}