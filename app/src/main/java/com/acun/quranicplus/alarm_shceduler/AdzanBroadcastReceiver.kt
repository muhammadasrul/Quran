package com.acun.quranicplus.alarm_shceduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import com.acun.quranicplus.R
import com.acun.quranicplus.util.EXTRA_ALARM

class AdzanBroadcastReceiver: BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "Prayer Reminder"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val name = intent?.getStringExtra(EXTRA_ALARM)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle("Prayer Reminder")
            .setContentText("Now it's time for $name prayer")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            }

            notificationManager?.createNotificationChannel(channel)
        }

        notificationManager?.notify(name.hashCode(), notification.build())

        val audioResource = if (name == "Fajr") R.raw.adzan_fajr else R.raw.adzan
        MediaPlayer
            .create(context, audioResource)
            .apply {
                isLooping = false
                start()
            }
    }
}