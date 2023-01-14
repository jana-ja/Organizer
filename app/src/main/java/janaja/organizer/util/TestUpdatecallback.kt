package janaja.organizer.util

import android.util.Log
import androidx.recyclerview.widget.ListUpdateCallback

class TestUpdatecallback: ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
        Log.i("lol", "$count inserted on $position")
    }

    override fun onRemoved(position: Int, count: Int) {
        Log.i("lol", "$count removed on $position")
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        Log.i("lol", "moved")
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        Log.i("lol", "$count changed on $position")
    }
}