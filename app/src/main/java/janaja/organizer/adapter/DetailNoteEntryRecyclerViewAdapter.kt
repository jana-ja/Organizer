package janaja.organizer.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.R
import janaja.organizer.data.model.Line

class DetailNoteEntryRecyclerViewAdapter(var dataset: MutableList<Line>) :
    RecyclerView.Adapter<DetailNoteEntryRecyclerViewAdapter.ItemViewHolder>() {

    private lateinit var recyclerView: RecyclerView

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lineText: EditText = view.findViewById(R.id.detail_note_entry_line)
        val checkBox: CheckBox = view.findViewById(R.id.detail_note_entry_checkBox)
    }

    fun newLine() {
        dataset.add(Line("", false))
        notifyItemInserted(dataset.lastIndex)
    }

    fun addLine(position: Int, line: String) {
        dataset.add(position, Line(line, false))
        notifyItemInserted(position)
    }

    fun removeLine(position: Int) {
        dataset.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_note_entry_line, parent, false)
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

        holder.lineText.doAfterTextChanged {
            var newText = it.toString()
            if (it.toString().contains("\n")){
                // finish this line
                newText = newText.replace("\n","")
                holder.lineText.setText(newText)
                // add new Line
                addLine(holder.layoutPosition + 1, "")
            } else {
                line.text = newText
            }
        }

        holder.itemView.requestFocus()
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