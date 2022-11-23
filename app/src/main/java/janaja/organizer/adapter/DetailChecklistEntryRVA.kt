package janaja.organizer.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.R
import janaja.organizer.data.model.Line
import kotlin.random.Random

class DetailChecklistEntryRVA(var dataset: MutableList<Line>) :
    RecyclerView.Adapter<DetailChecklistEntryRVA.ItemViewHolder>() {

    private lateinit var recyclerView: RecyclerView

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnDel: Button = view.findViewById(R.id.detail_note_entry_del)
        val lineText: EditText = view.findViewById(R.id.detail_note_entry_line)
        val checkBox: CheckBox = view.findViewById(R.id.detail_note_entry_checkBox)
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

        holder.btnDel.setOnClickListener {
            if(dataset.size > 1)
                removeLine(holder.layoutPosition)
        }

        holder.lineText.doAfterTextChanged {
            val newText = it.toString()
            if (newText.contains("\n")){
                // finish this line
                val splitIndex = newText.indexOf("\n")
                val frontText = newText.substring(0,splitIndex)
                val backText = newText.substring(splitIndex + 1, newText.length)
                holder.lineText.setText(frontText)
                // add new Line
                addLine(holder.layoutPosition + 1, backText)
            } else {
                line.text = newText
            }
        }

        holder.itemView.requestFocus()
        //holder.lineText.setSelection(holder.lineText.length())
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