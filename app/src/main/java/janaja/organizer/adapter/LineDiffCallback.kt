package janaja.organizer.adapter

import androidx.recyclerview.widget.DiffUtil
import janaja.organizer.data.model.Line
import janaja.organizer.data.model.Note

class LineDiffCallback(private val oldList: List<Line>, private val newList: List<Line>): DiffUtil.Callback() {

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