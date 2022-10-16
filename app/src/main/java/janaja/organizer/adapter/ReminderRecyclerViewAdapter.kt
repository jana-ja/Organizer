package janaja.organizer.adapter

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import janaja.organizer.R
import janaja.organizer.data.model.Note
import janaja.organizer.ui.home.HomeFragmentDirections

class ReminderRecyclerViewAdapter(dataset: MutableList<Note>, handler: ContextualAppBarHandler) : NoteRecyclerViewAdapter(dataset, handler) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.card.layoutParams.width = holder.itemView.resources.getDimension(R.dimen.reminder_card_width).toInt()
        holder.card.layoutParams.height = LayoutParams.MATCH_PARENT
        holder.noteCl.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        holder.bodyRv.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun manageClickListeners(holder: ItemViewHolder, position: Int) {
        val note = dataset[position]

        val onClick: (View) -> Unit = {
            val navController = holder.itemView.findNavController()
            navController.navigate(
                HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment(
                    note.id
                )
            )
        }

        // handle cardview
        holder.card.setOnClickListener(onClick)

        // handle recyclerview
        holder.bodyRv.setOnClickListener(onClick)
        holder.bodyRv.setOnTouchListener { _, e ->
            val time = SystemClock.uptimeMillis() - e.downTime
            when (e.action) {
                MotionEvent.ACTION_UP -> {
                    if (time < ViewConfiguration.getLongPressTimeout().toLong()) {
                        holder.bodyRv.callOnClick()
                    }
                }
            }
            true
        }
    }

}