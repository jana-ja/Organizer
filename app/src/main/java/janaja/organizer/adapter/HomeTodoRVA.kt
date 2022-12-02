package janaja.organizer.adapter

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.*
import android.widget.LinearLayout
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
    RecyclerView.Adapter<HomeTodoRVA.ItemViewHolder>() {

    var oldList = dataset.toList()
    var selected: MutableList<Boolean> = MutableList(dataset.size) { false }

    // called after dataset changed to display changes using DiffUtil
    fun updateList() {
        selected = MutableList(dataset.size) { false }
        TodoDiffCallback(oldList, dataset).also {
            DiffUtil.calculateDiff(it, false).dispatchUpdatesTo(this)
        }
        oldList = dataset.toList()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.todo_title)
        val bodyRv: RecyclerView = view.findViewById(R.id.todo_body_rv)
        val card: MaterialCardView = view.findViewById(R.id.todo_card)
        val scollingLl: LinearLayout = view.findViewById(R.id.todo_scrolling_ll)
        val addLine: ConstraintLayout = view.findViewById(R.id.add_line_cl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_item, parent, false)
        return ItemViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val todo = dataset[position]
        holder.title.text = todo.title

        val subadapter = HomeChecklistEntryRVA(todo.body)
        holder.bodyRv.adapter = subadapter

        holder.addLine.setOnClickListener {
             subadapter.addLine(todo.body.size, "")
        }
        // recyclerview, nestedscrollview and its parent cardview should have the same behaviour
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

        // handle scrollview
        holder.scollingLl.setOnClickListener(onClick)
        // TODO linear layout does not fill scroll layout so there is an area that is not clickable
    }

}