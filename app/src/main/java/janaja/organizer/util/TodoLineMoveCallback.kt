package janaja.organizer.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import janaja.organizer.adapter.DetailTodoEntryRVA

class TodoLineMoveCallback(val adapter: ItemTouchHelperInterface) : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // swipe disabled - do nothing
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN// or ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        adapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder is DetailTodoEntryRVA.ItemViewHolder){
                adapter.onRowSelected(viewHolder)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is DetailTodoEntryRVA.ItemViewHolder){
            adapter.onRowClear(viewHolder)
        }
    }



    interface ItemTouchHelperInterface {
        fun onRowMoved(fromPosition: Int, toPosition: Int)
        fun onRowSelected(viewHolder: DetailTodoEntryRVA.ItemViewHolder)
        fun onRowClear(viewHolder: DetailTodoEntryRVA.ItemViewHolder)
    }
}