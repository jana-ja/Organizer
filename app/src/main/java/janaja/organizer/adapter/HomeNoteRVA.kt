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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import janaja.organizer.R
import janaja.organizer.data.model.Note
import janaja.organizer.ui.home.HomeFragmentDirections
import janaja.organizer.ui.home.HomeNoteInterface
import janaja.organizer.util.NoteDiffCallback
import janaja.organizer.util.TestUpdatecallback
import kotlinx.coroutines.*

open class HomeNoteRVA(
    var dataset: MutableList<Note>,
    private val handler: ContextualAppBarHandler,
    private val homeNoteInterface: HomeNoteInterface
) :
    RecyclerView.Adapter<HomeNoteRVA.ItemViewHolder>() {

    var oldList = dataset.toList()
    private var selectedIndices: MutableList<Boolean> = MutableList(dataset.size) { false }
    private lateinit var mRecyclerView: RecyclerView

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.note_title)
        val body: TextView = view.findViewById(R.id.note_body)
        val bodyRv: RecyclerView = view.findViewById(R.id.todo_body_rv)
        val card: MaterialCardView = view.findViewById(R.id.todo_card)
        val noteCl: ConstraintLayout = view.findViewById(R.id.todo_cl)
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
            holder.bodyRv.adapter = HomeChecklistEntryRVA(note.body) { homeNoteInterface.updateNote(note) }
        } else {
            holder.body.text = note.body.joinToString(separator = "\n")
        }
        if(note.isPinned){
            val resources = holder.itemView.context.resources
            holder.card.strokeWidth = (resources.getDimension(R.dimen.card_view_stroke_width)).toInt()// / resources.displayMetrics.density).toInt()
        }

        // recyclerview and its parent cardview should have the same behaviour
        manageClickListeners(holder, position)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    // called after dataset changed to display changes using DiffUtil
    fun updateList() {
        selectedIndices = MutableList(dataset.size) { false }
        NoteDiffCallback(oldList, dataset).also {
            DiffUtil.calculateDiff(it, false).dispatchUpdatesTo(this)
        }
        oldList = dataset.toList()
    }


    private fun List<Note>.prettyPrint(): String {
        var string = ""
        for (i in this.indices){
            string += "\t$i: ${this[i]}\n"
        }
        return string
    }

    // for debug of diffcallback
    private val testUpdatecallback: TestUpdatecallback = TestUpdatecallback()
    fun updateList(notes: MutableList<Note>) {
        selectedIndices = MutableList(notes.size) { false }
        //Log.i("lol", "old:\n ${oldList.prettyPrint()}")
        //Log.i("lol", "new:\n ${notes.prettyPrint()}")
        dataset = notes
        // for debug: notifyDataSetChanged()
        NoteDiffCallback(oldList, dataset).also {
            DiffUtil.calculateDiff(it, false).dispatchUpdatesTo(this)// for debug: testUpdatecallback)
        }
        oldList = dataset.toList()
    }

    fun getSelectedIds(): List<Long> {
        val selectedIDs = mutableListOf<Long>()
        for (i in selectedIndices.indices) {
            if (selectedIndices[i]) selectedIDs.add(dataset[i].id)
        }
        return selectedIDs
    }

    @SuppressLint("ClickableViewAccessibility")
    fun manageClickListeners(holder: ItemViewHolder, position: Int) {
        val note = dataset[position]

        val onClick: (View) -> Unit = {
            Log.i("onClick", "selected count: ${selectedIndices.count { it }}")
            if (selectedIndices.count { it } > 0)
                onLongClick(holder)
            else {
                homeNoteInterface.loadNote(note.id)
                val navController = holder.itemView.findNavController()
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment())
            }
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

    private fun onLongClick(holder: ItemViewHolder) {
        val position = holder.layoutPosition // maybe adapterPosition??
        Log.i("onLongClick", "selected: ${selectedIndices[position]}")
        if (selectedIndices[position]) {
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
        selectedIndices[position] = !selectedIndices[position]
        handler.performSelectAction(selectedIndices.count { it })
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    fun unselectAll() {
        for (i in selectedIndices.indices) {
            if (selectedIndices[i]) {
                val holder = mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i)) as ItemViewHolder
                holder.card.strokeWidth = 0
                val typedValue = TypedValue()
                val theme: Theme = holder.itemView.context.theme
                theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
                holder.noteCl.setBackgroundColor(typedValue.data)
                selectedIndices[i] = false
            }
        }

    }

    interface ContextualAppBarHandler {
        fun performSelectAction(selectCount: Int)
    }
}