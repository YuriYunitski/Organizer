package com.yunitski.organizer

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ElementTouchListener(context: Context, private val listener: OnItemClickListener): RecyclerView.OnItemTouchListener {



    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    private var mGestureDetector: GestureDetector = GestureDetector(context, GestureDetector.SimpleOnGestureListener())

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView: View? = rv.findChildViewUnder(e.x, e.y)
        if (childView != null && mGestureDetector.onTouchEvent(e)){
            listener.onItemClick(childView, rv.getChildAdapterPosition(childView))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }
}