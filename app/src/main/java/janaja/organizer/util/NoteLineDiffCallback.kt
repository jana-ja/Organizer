package janaja.organizer.util

import androidx.recyclerview.widget.DiffUtil
import janaja.organizer.data.model.NoteLine

class NoteLineDiffCallback(private val oldList: List<NoteLine>, private val newList: List<NoteLine>): DiffUtil.Callback() {

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
        return oldList[oldItemPosition].equalContent(newList[newItemPosition])
    }

}