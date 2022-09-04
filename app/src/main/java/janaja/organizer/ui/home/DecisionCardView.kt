package janaja.organizer.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import janaja.organizer.R
import janaja.organizer.databinding.DecisionCardviewContentBinding
import janaja.organizer.databinding.DecisionCardviewHeaderBinding
import janaja.organizer.databinding.HomeCardviewBinding

class DecisionCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    val binding: HomeCardviewBinding
    val headerBinding: DecisionCardviewHeaderBinding
    val contentBinding: DecisionCardviewContentBinding

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
            R.layout.decision_cardview_header,
            binding.flHomeCardviewHeader,
            true
        )


        // manage content
        val params = binding.flHomeCardviewContent.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.flHomeCardviewContent.layoutParams = params
        contentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.decision_cardview_content,
            binding.flHomeCardviewContent,
            true
        )

    }
}