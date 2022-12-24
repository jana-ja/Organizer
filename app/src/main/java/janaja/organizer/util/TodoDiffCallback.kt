package janaja.organizer.util

import androidx.recyclerview.widget.DiffUtil
import janaja.organizer.data.model.Todo

class TodoDiffCallback(private val oldList: List<Todo>, private val newList: List<Todo>) : DiffUtil.Callback() {

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
        val oldBody = oldList[oldItemPosition].body
        val newBody = newList[newItemPosition].body
        // check lines of body
        if (oldBody.size != newBody.size)
            return false
        for (i in oldBody.indices) {
            if (oldBody[i].isChecked != newBody[i].isChecked)
                return false
        }
        return true
    }

}