package janaja.organizer.adapter

import android.app.ActionBar.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import janaja.organizer.R

class ReminderRecyclerViewAdapter(handler: ContextualAppBarHandler) : NoteRecyclerViewAdapter(handler) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.card.layoutParams.width = holder.itemView.resources.getDimension(R.dimen.reminder_card_width).toInt()
        holder.card.layoutParams.height = LayoutParams.MATCH_PARENT
        holder.noteCl.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        holder.bodyRv.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
    }

}