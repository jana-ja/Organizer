package janaja.organizer.adapter

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.R
import janaja.organizer.data.model.TodoLine

class HomeTodoEntryRVA(var dataset: MutableList<TodoLine>, val updateTodo: () -> Unit) : RecyclerView.Adapter<HomeTodoEntryRVA.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lineText: TextView = view.findViewById(R.id.note_entry_line)
        val checkBox: CheckBox = view.findViewById(R.id.note_entry_checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_entry_line, parent, false)
        return ItemViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val line = dataset[position]
        holder.lineText.text = line.text
        holder.checkBox.isChecked = line.isChecked
        if (line.isChecked) {
            holder.lineText.paintFlags = holder.lineText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.lineText.paintFlags = holder.lineText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.checkBox.setOnClickListener {
            line.isChecked = holder.checkBox.isChecked
            if (holder.checkBox.isChecked) {
                holder.lineText.paintFlags = holder.lineText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.lineText.paintFlags = holder.lineText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            updateTodo()
        }

        if (line.repeat) {
            holder.lineText.setTypeface(null, Typeface.BOLD)
        } else {
            holder.lineText.setTypeface(null, Typeface.NORMAL)
        }

        // display indentation level
        val startMargin = when(line.indentationLevel){
            1 -> holder.itemView.resources.getDimensionPixelOffset(R.dimen.todo_entry_startmargin_lvl1)
            2 -> holder.itemView.resources.getDimensionPixelOffset(R.dimen.todo_entry_startmargin_lvl2)
            else -> holder.itemView.resources.getDimensionPixelOffset(R.dimen.todo_entry_startmargin_lvl0)
        }
        holder.lineText.layoutParams.let { params ->
            if (params is ViewGroup.MarginLayoutParams){
                params.marginStart = startMargin
                holder.lineText.requestLayout()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}