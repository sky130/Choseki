package ml.sky233.choseki.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

object ViewUtils {
    @SuppressLint("ClickableViewAccessibility")
    fun View.addTouchScale() {
        setOnTouchListener { v: View, event: MotionEvent ->
            val scale = 0.9f
            val duration: Long = 150
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.animate().scaleX(scale).scaleY(scale)
                    .setDuration(duration).start()

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.animate().scaleX(1f)
                    .scaleY(1f).setDuration(duration).start()

                else -> {}
            }
            v.onTouchEvent(event)
        } //today is crazy Thursday, who vivo 50 to have KFC?
    }

    fun setText(view: TextView, c: CharSequence?) {
        view.text = c
    }
}