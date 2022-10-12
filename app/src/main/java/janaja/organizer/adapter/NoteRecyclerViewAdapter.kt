package janaja.organizer.adapter

import android.annotation.SuppressLint
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.R
import janaja.organizer.data.model.Note
import janaja.organizer.ui.home.HomeFragmentDirections
import kotlinx.coroutines.*

open class NoteRecyclerViewAdapter :
    RecyclerView.Adapter<NoteRecyclerViewAdapter.ItemViewHolder>() {

    open var dataset: MutableList<Note> = mutableListOf()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.note_title)
        val body: TextView = view.findViewById(R.id.note_body)
        val bodyRv: RecyclerView = view.findViewById(R.id.note_body_rv)
        val card: CardView = view.findViewById(R.id.note_card)
        val noteCl: ConstraintLayout = view.findViewById(R.id.note_cl)
    }
    var job: Job? = null



    @SuppressLint("NotifyDataSetChanged")
    fun submitList(noteList: MutableList<Note>) {
        dataset = noteList
        notifyDataSetChanged()
    }

    fun addItem(note: Note) {
        dataset.add(0, note)
        notifyItemInserted(0)
    }

    fun removeitem(position: Int) {
        dataset.removeAt(position)
        notifyItemRemoved(position)
    }

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
        manageClickListeners(holder, note)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun manageClickListeners(holder: ItemViewHolder, note: Note) {
        val onClick: (View) -> Unit = {
            val navController = holder.itemView.findNavController()
            navController.navigate(
                HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment(
                    note.id
                )
            )
        }
        val onLongClick: (holder: ItemViewHolder) -> Unit = {
            holder.card.cardElevation = 50F
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

    override fun getItemCount(): Int {
        return dataset.size
    }
}