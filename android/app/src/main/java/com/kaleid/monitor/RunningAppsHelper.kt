package com.kaleid.monitor

import android.app.ActivityManager
import android.content.Context

object RunningAppsHelper {
    fun getRunningApps(context: Context): List<String> {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.runningAppProcesses ?: return emptyList()

        return runningTasks.map { it.processName }
    }
}
