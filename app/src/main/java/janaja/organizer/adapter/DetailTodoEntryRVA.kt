package janaja.organizer.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.R
import janaja.organizer.data.model.TodoLine
import janaja.organizer.util.TodoDetailCallback
import janaja.organizer.util.TodoLineMoveCallback
import java.util.*

class DetailTodoEntryRVA(var dataset: MutableList<TodoLine>, private val callbackInterface: TodoDetailCallback) :
    RecyclerView.Adapter<DetailTodoEntryRVA.ItemViewHolder>(), TodoLineMoveCallback.ItemTouchHelperInterface {

    private lateinit var recyclerView: RecyclerView

    private var insert = false
    private var insertEnd = false

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cv: CardView = view.findViewById(R.id.detail_todo_entry_cv)
        val btnDel: ImageButton = view.findViewById(R.id.detail_todo_entry_del_btn)
        val lineText: EditText = view.findViewById(R.id.detail_todo_entry_line)
        val checkBox: CheckBox = view.findViewById(R.id.detail_todo_entry_checkBox)
        val repeat: ImageButton = view.findViewById(R.id.detail_todo_entry_repeat_btn)
    }

    fun addLine(position: Int = dataset.size, line: String = "") {
        insert = true
        dataset.add(position, TodoLine(line))
        notifyItemInserted(position)
    }

    fun addLineEnd(){
        insertEnd = true
        addLine()
    }

    private fun removeLine(position: Int) {
        dataset.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitNewList(newList: MutableList<TodoLine>){
        dataset = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_todo_entry_line, parent, false)
        return ItemViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val line = dataset[position]

        // display text content
        holder.lineText.setText(line.text)

        // display checkbox state
        holder.checkBox.isChecked = line.isChecked
        if (line.isChecked) {
            holder.lineText.paintFlags = holder.lineText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.lineText.paintFlags =
                holder.lineText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // display repeat state
        if (line.repeat) {
            holder.repeat.setImageResource(R.drawable.repeat)
            holder.lineText.setTypeface(null, Typeface.BOLD)
        }

        // remove line on button click
        holder.btnDel.setOnClickListener {
            if (dataset.size > 1)
                removeLine(holder.layoutPosition)
        }

        // insert new line on enter
        holder.lineText.doAfterTextChanged {
            val newText = it.toString()
            if (newText.contains("\n")) {
                // finish this line
                val splitIndex = newText.indexOf("\n")
                val frontText = newText.substring(0, splitIndex)
                val backText = newText.substring(splitIndex + 1, newText.length)
                holder.lineText.setText(frontText)
                // add new Line
                addLine(holder.layoutPosition + 1, backText)
            } else {
                line.text = newText
            }
        }

        // toggle checkbox on click
        holder.checkBox.setOnClickListener {
            line.isChecked = holder.checkBox.isChecked
            if (holder.checkBox.isChecked) {
                holder.lineText.paintFlags =
                    holder.lineText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.lineText.paintFlags =
                    holder.lineText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        // toggle repeat on click
        holder.repeat.setOnClickListener {
            if (line.repeat) {
                holder.repeat.setImageResource(R.drawable.repeat_off)
                holder.lineText.setTypeface(null, Typeface.NORMAL)
            } else {
                holder.repeat.setImageResource(R.drawable.repeat)
                holder.lineText.setTypeface(null, Typeface.BOLD)
            }
            line.repeat = !line.repeat
            //updateList()
        }

        // if this line is inserted as new line, request focus and show keyboard
        // case A: insert not at the end (insertEnd is false)
        // case B: insert at the end and this item is at the end
        if(insert) {
            if(!insertEnd || position == dataset.lastIndex) {
                callbackInterface.showSoftKeyboard(holder.lineText)
                insert = false
                insertEnd = false
            }
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getAllLines(): MutableList<TodoLine> {
        return dataset
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    // drag and drop interface
    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dataset, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dataset, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(viewHolder: ItemViewHolder) {
        viewHolder.cv.cardElevation = 8f
    }

    override fun onRowClear(viewHolder: ItemViewHolder) {
        viewHolder.cv.cardElevation = 0f
    }
}