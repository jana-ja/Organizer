package janaja.organizer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.R
import janaja.organizer.data.model.Note

class NoteRecyclerViewAdapter : RecyclerView.Adapter<NoteRecyclerViewAdapter.ItemViewHolder>() {

    private var dataset = mutableListOf<Note>()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.note_title)
        val body: TextView = view.findViewById(R.id.note_body)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(noteList: MutableList<Note>){
        dataset = noteList
        notifyDataSetChanged()
    }

    fun addItem(note: Note){
        dataset.add(0, note)
        notifyItemInserted(0)
    }

    fun removeitem(position: Int){
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
        holder.body.text = note.body
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}