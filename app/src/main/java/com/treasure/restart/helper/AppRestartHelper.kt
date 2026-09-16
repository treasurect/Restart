package com.treasure.restart.helper

import android.content.Context
import android.content.Intent
import android.os.Process

object AppRestartHelper {

    fun restart(context: Context) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        launchIntent?.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        context.startActivity(launchIntent)
        Process.killProcess(Process.myPid())
    }
}
