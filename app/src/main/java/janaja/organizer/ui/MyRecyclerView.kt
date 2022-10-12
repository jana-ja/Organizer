package janaja.organizer.ui

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    var job: Job? = null


    suspend fun detectLongPress() {

        delay(ViewConfiguration.getLongPressTimeout().toLong())

        Log.i("coroutine", "detected long press")
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        Log.i("onTouchEvent", e.toString())

        val time = SystemClock.uptimeMillis() - e.downTime

        when (e.getAction()) {

            MotionEvent.ACTION_DOWN -> {
//                Log.i("TAG", "action down: $time")

                job = findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    detectLongPress()
                }

            }
            MotionEvent.ACTION_UP -> {
//                Log.i("TAG", "action up: $time")
                job?.cancel()
                if (time < ViewConfiguration.getLongPressTimeout().toLong()) {
                    Log.i("TAG", "onClick detected: $time")
//                callOnClick()
                }

            }
            MotionEvent.ACTION_CANCEL -> job?.cancel()
        }
        return true


//        Log.i("onTouchEvent", e.toString())
//
//        if (e.action == MotionEvent.ACTION_UP) {
//            val time = SystemClock.uptimeMillis() - e.downTime
//            Log.i("TAG", "action up: $time")
//
//            if (time < ViewConfiguration.getLongPressTimeout().toLong()) {
//                Log.i("TAG", "onClick: $time")
//                callOnClick()
//
//            }
//        }
//        if(e.action == MotionEvent.ACTION_MOVE || e.action == MotionEvent.ACTION_DOWN ){}
//            // this gesture detector only handle click and long press
//            GestureDetector(context, MyGestureListener()).onTouchEvent(e)
//        return true
    }
//
//    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
//        /*
//         * This method JUST determines whether we want to intercept the motion.
//         * If we return true, onTouchEvent will be called and we do the actual
//         * scrolling there.
//         */
//        //onTouchEvent(e)
//        return true
////        return when (e.actionMasked) {
//////            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
//////                // handle touch
//////                onTouchEvent(e)
//////                // but also let child handle it
//////                true
//////            }
////            else -> {
////                // In general, we don't want to intercept touch events. They should be
////                // handled by the child view.
////                true
////            }
////        }
//    }
}

class MyGestureListener() : GestureDetector.SimpleOnGestureListener() {

    override fun onDown(event: MotionEvent): Boolean {
        Log.d("gesturelistener", "onDown: ");

        // don't return false here or else none of the other
        // gestures will work
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        Log.i("gesturelistener", "onSingleTapUp: ")
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        Log.i("gesturelistener", "onSingleTapConfirmed: ")
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        val time = SystemClock.uptimeMillis() - event.downTime

        Log.i("longpress", "onLongPress: $time")


        super.onLongPress(event)
    }

}