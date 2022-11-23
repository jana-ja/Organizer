package janaja.organizer.adapter

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.R
import janaja.organizer.data.model.Line
import janaja.organizer.util.LineDiffCallback
import kotlin.random.Random

class DetailTodoEntryRVA(var dataset: MutableList<Line>) :
    RecyclerView.Adapter<DetailTodoEntryRVA.ItemViewHolder>() {

    private lateinit var recyclerView: RecyclerView
    var editable: Boolean = false

    private var oldList: List<Line> = dataset.map { it.copyLine() }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnDel: Button = view.findViewById(R.id.detail_todo_entry_del)
        val lineText: EditText = view.findViewById(R.id.detail_todo_entry_line)
        val checkBox: CheckBox = view.findViewById(R.id.detail_todo_entry_checkBox)
        val repeat: ImageButton = view.findViewById(R.id.detail_todo_entry_repeat)
    }

    fun addLine(position: Int, line: String) {
        // TODO richtige ID
        dataset.add(position, Line(Random.nextLong(), line, false))
        notifyItemInserted(position)
    }

    fun removeLine(position: Int) {
        dataset.removeAt(position)
        notifyItemRemoved(position)
    }

    // called after dataset changed to display changes using DiffUtil
    fun updateList() {
        LineDiffCallback(oldList, dataset).also {
            DiffUtil.calculateDiff(it, false).dispatchUpdatesTo(this)
        }
        oldList = dataset.map { it.copyLine() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_todo_entry_line, parent, false)
        return ItemViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val line = dataset[position]
        holder.lineText.setText(line.text)
        holder.checkBox.isChecked = line.isChecked
        if (line.isChecked) {
            holder.lineText.paintFlags = holder.lineText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.lineText.paintFlags =
                holder.lineText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.btnDel.setOnClickListener {
            if (dataset.size > 1)
                removeLine(holder.layoutPosition)
        }

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

        holder.repeat.setOnClickListener {
            if (line.repeat) {
                holder.repeat.setImageResource(R.drawable.repeat_off)
                holder.lineText.setTypeface(null, Typeface.NORMAL)
            } else {
                holder.repeat.setImageResource(R.drawable.repeat)
                holder.lineText.setTypeface(null, Typeface.BOLD)
            }
            line.repeat = !line.repeat
            updateList()
        }

        if (line.repeat) {
            holder.repeat.setImageResource(R.drawable.repeat)
            holder.lineText.setTypeface(null, Typeface.BOLD)
        }

        if (line.repeat && !editable) {
            // not editable
            holder.lineText.isEnabled = false
            holder.btnDel.isEnabled = false
        } else {
            holder.itemView.requestFocus()
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getAllLines(): MutableList<Line> {
        return dataset
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
}