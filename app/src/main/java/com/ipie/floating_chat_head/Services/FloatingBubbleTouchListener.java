package com.ipie.floating_chat_head.Services;

import android.view.View;

/**
 * Floating bubble remove listener
 * Created by bijoysingh on 2/19/17.
 */

public interface FloatingBubbleTouchListener {

  void onDown(float x, float y);

  void onTap(boolean expanded);

  void onRemove();

  void onMove(float x, float y);

  void onUp(float x, float y);

  void onTouch(View.OnClickListener listener);

}
