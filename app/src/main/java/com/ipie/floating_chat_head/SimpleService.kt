package com.ipie.floating_chat_head

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.ipie.floating_chat_head.Services.FloatingBubbleConfig
import com.ipie.floating_chat_head.Services.FloatingBubbleService

open class SimpleService : FloatingBubbleService() {

    var user_infoInS: String? = null
    var user_number: String? = null
    var user_id: String? = null
    var user_name: String? = null
    var user_token: String? = null
    var qr_token: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            user_number = intent.getStringExtra("user_number")
            user_id = intent.getStringExtra("user_id")
            user_name = intent.getStringExtra("user_name")
            user_token = intent.getStringExtra("user_token")
            qr_token = intent.getStringExtra("qr_token")
            Log.e("user_number", " In Native ==== user_number$user_number")
            Log.e("user_id", "  In Native ==== user_id$user_id")
            Log.e("user_name", "  In Native ==== user_name$user_name")
            Log.e("user_token", "  In Native ==== user_token$user_token")
            Log.e("qr_token", "  In Native ==== qr_token$qr_token")
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        Log.e("SimpleService", " onDestroy " + "Called")
        super.onDestroy()
    }


    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("SimpleService", " onUnbind " + "Called")
        return super.onUnbind(intent)
    }


    override var config: FloatingBubbleConfig? = null
        get() {
            val context = applicationContext
            return FloatingBubbleConfig.Builder()
                .removeBubbleIcon(ContextCompat.getDrawable(context, R.drawable.close_default_icon))
                .bubbleIconDp(100)
                .expandableView(getInflater()!!.inflate(R.layout.sample_view_1, null))
                .paddingDp(2)
                .borderRadiusDp(0)
                .physicsEnabled(true)
                .expandableColor(Color.WHITE)
                .triangleColor(-0xdea59c)
                .gravity(Gravity.LEFT)
                .bubbleUsername(user_name)
                .bubbleUserID(user_id)
                .bubbleUserToken(user_token)
                .bubbleUserNumber(user_number)
                .qrCodeToken(qr_token)
                .build()
        }


}