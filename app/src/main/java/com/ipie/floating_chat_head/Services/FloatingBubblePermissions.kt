package com.ipie.floating_chat_head.Services

import android.os.Build
import android.app.Activity
import android.content.Context
import com.ipie.floating_chat_head.Services.FloatingBubblePermissions
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Utilities for handling permissions
 * Created by bijoy on 1/6/17.
 */
object FloatingBubblePermissions {
    const val REQUEST_CODE_ASK_PERMISSIONS = 1201

    /**
     * Checks if the permissions is required
     *
     * @param context the application context
     * @return is the permission request needed
     */
    fun requiresPermission(context: Context?): Boolean {
        return Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)
    }

    /**
     * Start the permission request
     *
     * @param activity the activity
     */
    fun startPermissionRequest(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23 && requiresPermission(activity)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.packageName)
            )
            activity.startActivityForResult(
                intent,
                REQUEST_CODE_ASK_PERMISSIONS
            )
        }
    }
}