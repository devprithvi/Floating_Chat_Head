package com.ipie.floating_chat_head.Services

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.ipie.floating_chat_head.Services.FloatingBubbleConfig

/**
 * Floating configurations
 * Created by bijoysingh on 2/19/17.
 */
open class FloatingBubbleConfig private constructor(builder: Builder) {
    val bubbleIcon: Drawable?
    val removeBubbleIcon: Drawable?
    val expandableView: View?
    val bubbleIconDp: Int
    val removeBubbleIconDp: Int
    val removeBubbleAlpha: Float
    val expandableColor: Int
    val triangleColor: Int
    val gravity: Int
    val paddingDp: Int
    val borderRadiusDp: Int
    val isPhysicsEnabled: Boolean
    private val listener: View.OnClickListener?
    val userName: String?
    val token: String?
    val qrCodeToken: String?
    val userID: String?
    val userNumber: String? = null

    class Builder {
        internal var bubbleIcon: Drawable? = null
        internal var removeBubbleIcon: Drawable? = null
        internal var expandableView: View? = null
        internal var bubbleIconDp = 64
        internal var removeBubbleIconDp = 64
        internal var expandableColor = Color.WHITE
        internal var triangleColor = Color.WHITE
        internal var gravity = Gravity.END
        internal var paddingDp = 4
        internal var borderRadiusDp = 4
        internal var removeBubbleAlpha = 1.0f
        internal var physicsEnabled = true
        internal var username: String? = null
        internal var Token: String? = null
        internal var QrCodeToken: String? = null
        internal var UserID: String? = null
        private var userNumber: String? = null
        var listener: View.OnClickListener? = null
        fun bubbleUsername(username: String?): Builder {
            this.username = username
            return this
        }

        fun bubbleUserID(UserID: String?): Builder {
            this.UserID = UserID
            return this
        }

        fun bubbleUserNumber(userNumber: String?): Builder {
            this.userNumber = userNumber
            return this
        }

        fun bubbleUserToken(Token: String?): Builder {
            this.Token = Token
            return this
        }

        fun qrCodeToken(QrCodeToken: String?): Builder {
            this.QrCodeToken = QrCodeToken
            return this
        }

        fun bubbleIcon(`val`: Drawable?): Builder {
            bubbleIcon = `val`
            return this
        }

        fun removeBubbleIcon(`val`: Drawable?): Builder {
            removeBubbleIcon = `val`
            return this
        }

        fun clickView(listener_: View.OnClickListener?): Builder {
            listener = listener_
            return this
        }

        fun expandableView(`val`: View?): Builder {
            expandableView = `val`
            return this
        }

        fun bubbleIconDp(`val`: Int): Builder {
            bubbleIconDp = `val`
            return this
        }

        fun removeBubbleIconDp(`val`: Int): Builder {
            removeBubbleIconDp = `val`
            return this
        }

        fun triangleColor(`val`: Int): Builder {
            triangleColor = `val`
            return this
        }

        fun expandableColor(`val`: Int): Builder {
            expandableColor = `val`
            return this
        }

        fun build(): FloatingBubbleConfig {
            return FloatingBubbleConfig(this)
        }

        fun gravity(`val`: Int): Builder {
            gravity = `val`
            if (gravity == Gravity.CENTER || gravity == Gravity.CENTER_VERTICAL || gravity == Gravity.CENTER_HORIZONTAL) {
                gravity = Gravity.CENTER_HORIZONTAL
            } else if (gravity == Gravity.TOP ||
                gravity == Gravity.BOTTOM
            ) {
                gravity = Gravity.END
            }
            return this
        }

        fun paddingDp(`val`: Int): Builder {
            paddingDp = `val`
            return this
        }

        fun borderRadiusDp(`val`: Int): Builder {
            borderRadiusDp = `val`
            return this
        }

        fun physicsEnabled(`val`: Boolean): Builder {
            physicsEnabled = `val`
            return this
        }

        fun removeBubbleAlpha(`val`: Float): Builder {
            removeBubbleAlpha = `val`
            return this
        }
    }

    companion object {
        fun getDefaultBuilder(context: Context?): Builder {
            return Builder()
                .bubbleIconDp(100)
                .removeBubbleIconDp(64)
                .paddingDp(4)
                .removeBubbleAlpha(1.0f)
                .physicsEnabled(true)
                .expandableColor(Color.WHITE)
                .triangleColor(Color.WHITE)
                .gravity(Gravity.END)
        }

        //
        //   .bubbleIcon(ContextCompat.getDrawable(context, R.drawable.bubble_default_icon))
        //          .removeBubbleIcon(ContextCompat.getDrawable(context, R.drawable.close_default_icon))
        fun getDefault(context: Context?): FloatingBubbleConfig {
            return getDefaultBuilder(context).build()
        }
    }

    init {
        bubbleIcon = builder.bubbleIcon
        removeBubbleIcon = builder.removeBubbleIcon
        expandableView = builder.expandableView
        bubbleIconDp = builder.bubbleIconDp
        removeBubbleIconDp = builder.removeBubbleIconDp
        expandableColor = builder.expandableColor
        triangleColor = builder.triangleColor
        gravity = builder.gravity
        paddingDp = builder.paddingDp
        borderRadiusDp = builder.borderRadiusDp
        isPhysicsEnabled = builder.physicsEnabled
        removeBubbleAlpha = builder.removeBubbleAlpha
        listener = builder.listener
        userName = builder.username
        token = builder.Token
        qrCodeToken = builder.QrCodeToken
        userID = builder.UserID
    }
}