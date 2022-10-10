package janaja.organizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.R
import kotlin.random.Random

class NoteEntryRecyclerViewAdapter(var dataset: MutableList<String>) : RecyclerView.Adapter<NoteEntryRecyclerViewAdapter.ItemViewHolder>(){

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val line: CheckedTextView = view.findViewById(R.id.note_entry_line)
    }

    fun newLine(){
        dataset.add("")
        notifyItemInserted(dataset.lastIndex)
    }

    fun addItem(line: String){
        dataset.add(line)
        notifyItemInserted(dataset.lastIndex)
    }

    fun removeitem(position: Int){
        dataset.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_entry_line, parent, false)
        return ItemViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val line = dataset[position]
        holder.line.text = line
        if(Random.nextInt() % 2 == 0)
            holder.line.isChecked
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}