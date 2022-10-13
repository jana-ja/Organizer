package janaja.organizer.adapter

import android.annotation.SuppressLint
import android.content.res.Resources.Theme
import android.os.SystemClock
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import janaja.organizer.R
import janaja.organizer.data.model.Note
import janaja.organizer.ui.home.HomeFragmentDirections
import kotlinx.coroutines.*

open class NoteRecyclerViewAdapter(private val handler: ContextualAppBarHandler) :
    RecyclerView.Adapter<NoteRecyclerViewAdapter.ItemViewHolder>() {

    open var dataset: MutableList<Note> = mutableListOf()
    var selected: MutableList<Boolean> = mutableListOf()
    private lateinit var mRecyclerView: RecyclerView

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.note_title)
        val body: TextView = view.findViewById(R.id.note_body)
        val bodyRv: RecyclerView = view.findViewById(R.id.note_body_rv)
        val card: MaterialCardView = view.findViewById(R.id.note_card)
        val noteCl: ConstraintLayout = view.findViewById(R.id.note_cl)
    }
    var job: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_list_item, parent, false)
        return ItemViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note = dataset[position]
        holder.title.text = note.title
        if (note.isCheckList) {
            holder.body.visibility = View.GONE
            holder.bodyRv.visibility = View.VISIBLE
            holder.bodyRv.adapter = NoteEntryRecyclerViewAdapter(note.body)
        } else {
            holder.body.text = note.body.joinToString(separator = "\n")
        }

        // recyclerview and its parent cardview should have the same behaviour
        manageClickListeners(holder, position)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(noteList: MutableList<Note>) {
        dataset = noteList
        selected = MutableList(dataset.size){false}
        notifyDataSetChanged()
    }

    fun addItem(note: Note) {
        dataset.add(0, note)
        selected.add(0,false)
        notifyItemInserted(0)
    }

    fun removeitem(position: Int) {
        dataset.removeAt(position)
        selected.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun manageClickListeners(holder: ItemViewHolder, position: Int) {
        val note = dataset[position]

        val onClick: (View) -> Unit = {
            val navController = holder.itemView.findNavController()
            navController.navigate(
                HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment(
                    note.id
                )
            )
        }
        val onLongClick: (holder: ItemViewHolder) -> Unit = {
            if(selected[position]){
                // is already selected
                holder.card.strokeWidth = 0
                val typedValue = TypedValue()
                val theme: Theme = holder.itemView.context.theme
                theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
                holder.noteCl.setBackgroundColor(typedValue.data)
            } else {
                // is not selected
                holder.card.strokeWidth = 3
                val typedValue = TypedValue()
                val theme: Theme = holder.itemView.context.theme
                theme.resolveAttribute(com.google.android.material.R.attr.colorSurfaceVariant, typedValue, true)
                holder.noteCl.setBackgroundColor(typedValue.data)
            }
            selected[position] = !selected[position]
            handler.selectAction(selected.count{it})
        }

        // handle cardview
        holder.card.setOnClickListener(onClick)
        holder.card.setOnLongClickListener {
            onLongClick(holder)
            true
        }

        // handle recyclerview
        holder.bodyRv.setOnClickListener(onClick)
        // detect longClick is complicated. GestureDetector didn't work (detecting LonPress every time no matter what)
        // start coroutine that's waiting for 400ms (Androids LongPressTimeOut)
        // if UP or CANCEL movement is detected, the coroutine is stopped.
        // if it finishes waiting then a long click is detected.
        holder.bodyRv.setOnTouchListener { _, e ->
            val time = SystemClock.uptimeMillis() - e.downTime
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    job = holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                        delay(ViewConfiguration.getLongPressTimeout().toLong())
                        onLongClick(holder)
                        Log.i("coroutine", "detected long press")
                    }
                }
                MotionEvent.ACTION_UP -> {
                    job?.cancel()
                    if (time < ViewConfiguration.getLongPressTimeout().toLong()) {
                        holder.bodyRv.callOnClick()
                    }
                }
                MotionEvent.ACTION_CANCEL -> job?.cancel()
            }
            true
        }
    }
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }
    fun unselectAll(){
        for (i in selected.indices){
            if(selected[i]){
                val holder = mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i)) as ItemViewHolder
                holder.card.strokeWidth = 0
                val typedValue = TypedValue()
                val theme: Theme = holder.itemView.context.theme
                theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
                holder.noteCl.setBackgroundColor(typedValue.data)
            }
        }

    }

    interface ContextualAppBarHandler{
        fun selectAction(selectCount: Int)
    }
}