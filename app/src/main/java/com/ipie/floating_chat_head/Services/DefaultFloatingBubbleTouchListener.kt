package com.ipie.floating_chat_head.Services

import android.view.View

/**
 * Created by bijoysingh on 2/19/17.
 */
open class DefaultFloatingBubbleTouchListener : FloatingBubbleTouchListener {
    override fun onDown(x: Float, y: Float) {}
    override fun onTap(expanded: Boolean) {}
    override fun onRemove() {}
    override fun onMove(x: Float, y: Float) {}
    override fun onUp(x: Float, y: Float) {}
    override fun onTouch(listener: View.OnClickListener) {}
}