package janaja.organizer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.R
import janaja.organizer.data.model.Note
import janaja.organizer.ui.home.HomeFragmentDirections

open class NoteRecyclerViewAdapter : RecyclerView.Adapter<NoteRecyclerViewAdapter.ItemViewHolder>() {

    open var dataset: MutableList<Note> = mutableListOf()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.note_title)
        val body: TextView = view.findViewById(R.id.note_body)
        val bodyRv: RecyclerView = view.findViewById(R.id.note_body_rv)
        val card: CardView = view.findViewById(R.id.note_card)
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
        if(note.isCheckList){
            holder.body.visibility = View.GONE
            holder.bodyRv.visibility = View.VISIBLE
            holder.bodyRv.adapter = NoteEntryRecyclerViewAdapter(note.body)
        } else {
            holder.body.text = note.body.joinToString(separator = "\n")
        }

        holder.card.setOnClickListener{
            val navController = holder.itemView.findNavController()

            navController.navigate(HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment(note.id))

        }
        holder.card.setOnLongClickListener {
            holder.card.elevation
            // TODO show as marked
            // TODO show different app bar
            // TODO manage data
            Toast.makeText(holder.itemView.context, "he", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}