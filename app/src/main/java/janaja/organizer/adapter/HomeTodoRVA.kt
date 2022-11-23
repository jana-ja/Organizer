package janaja.organizer.adapter

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.os.SystemClock
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import janaja.organizer.R
import janaja.organizer.data.model.Todo
import janaja.organizer.ui.home.HomeFragmentDirections
import janaja.organizer.util.TodoDiffCallback

class HomeTodoRVA(var dataset: MutableList<Todo>) :
    RecyclerView.Adapter<HomeTodoRVA.ItemViewHolder>()  {

    var oldList = dataset.toList()
    var selected: MutableList<Boolean> = MutableList(dataset.size){false}

    // called after dataset changed to display changes using DiffUtil
    fun updateList() {
        selected = MutableList(dataset.size){false}
        TodoDiffCallback(oldList, dataset).also{
            DiffUtil.calculateDiff(it, false).dispatchUpdatesTo(this)
        }
        oldList = dataset.toList()
    }
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.note_title)
        val body: TextView = view.findViewById(R.id.note_body)
        val bodyRv: RecyclerView = view.findViewById(R.id.note_body_rv)
        val card: MaterialCardView = view.findViewById(R.id.note_card)
        val noteCl: ConstraintLayout = view.findViewById(R.id.note_cl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_list_item, parent, false)
        return ItemViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note = dataset[position]
        holder.title.text = note.title

            holder.body.visibility = View.GONE
            holder.bodyRv.visibility = View.VISIBLE
            holder.bodyRv.adapter = HomeChecklistEntryRVA(note.body)


        holder.card.layoutParams.width = holder.itemView.resources.getDimension(R.dimen.reminder_card_width).toInt()
        holder.card.layoutParams.height = LayoutParams.MATCH_PARENT
        holder.noteCl.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        holder.bodyRv.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT

        // recyclerview and its parent cardview should have the same behaviour
        manageClickListeners(holder, position)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    @SuppressLint("ClickableViewAccessibility")
    fun manageClickListeners(holder: ItemViewHolder, position: Int) {
        val note = dataset[position]

        val onClick: (View) -> Unit = {
            val navController = holder.itemView.findNavController()
            navController.navigate(
                HomeFragmentDirections.actionHomeFragmentToTodoDetailFragment(
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