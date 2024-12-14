package com.kaleid.monitor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class ForegroundService : Service() {
    private val client = OkHttpClient()
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate() {
        super.onCreate()
        handler = Handler(mainLooper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val serverAddress = intent?.getStringExtra("SERVER_ADDRESS") ?: ""
        if (serverAddress.isBlank()) {
            stopSelf()
            return START_NOT_STICKY
        }

        startForegroundService()
        runnable = object : Runnable {
            override fun run() {
                val runningApps = RunningAppsHelper.getRunningApps(this@ForegroundService)
                uploadDataToServer(serverAddress, runningApps)
                handler.postDelayed(this, 5000) // 每隔5秒检测一次
            }
        }
        handler.post(runnable)

        return START_STICKY
    }

    private fun uploadDataToServer(serverAddress: String, runningApps: List<String>) {
        try {
            val json = JSONObject()
            json.put("running_apps", runningApps)

            val requestBody = RequestBody.create(
                "application/json".toMediaType(),
                json.toString()
            )
            val request = Request.Builder()
                .url(serverAddress)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                Log.i("ForegroundService", "Server response: ${response.body?.string()}")
            }
        } catch (e: Exception) {
            Log.e("ForegroundService", "Error uploading data", e)
        }
    }

    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "RunningAppMonitorChannel"
            val channelName = "Running App Monitor Service"
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(notificationChannel)
            val notification = Notification.Builder(this, channelId)
                .setContentTitle("Running App Monitor")
                .setContentText("Monitoring running applications...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()
            startForeground(1, notification)
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
