package janaja.organizer.util

import androidx.recyclerview.widget.DiffUtil
import janaja.organizer.data.model.Todo

class TodoDiffCallback(private val oldList: List<Todo>, private val newList: List<Todo>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // TODO muss vllt detaillierter
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}