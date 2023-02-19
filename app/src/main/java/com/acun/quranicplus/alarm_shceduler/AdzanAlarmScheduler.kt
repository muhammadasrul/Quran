package com.acun.quranicplus.alarm_shceduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.data.remote.response.prayer.model.hour
import com.acun.quranicplus.data.remote.response.prayer.model.minute
import com.acun.quranicplus.util.EXTRA_ALARM
import java.util.Calendar

class AdzanAlarmScheduler(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    override fun scheduler(item: Prayer) {

        val time = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, item.hour().plus(1))
            set(Calendar.MINUTE, item.minute().plus(8))
        }.time.time

        Log.d("waduh", "time: $time")

        val intent = Intent(context, AdzanBroadcastReceiver::class.java).apply {
            putExtra(EXTRA_ALARM, item.name)
        }

        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time,
            AlarmManager.INTERVAL_DAY,
            PendingIntent.getBroadcast(
                context,
                item.name.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        )

        Toast.makeText(context, "Alarm ${item.name} is set", Toast.LENGTH_SHORT).show()
    }

    override fun cancel(item: Prayer) {
        alarmManager?.cancel(
            PendingIntent.getBroadcast(
                context,
                item.name.hashCode(),
                Intent(context, AdzanBroadcastReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        )

        Toast.makeText(context, "Alarm ${item.name} is cancel", Toast.LENGTH_SHORT).show()
    }
}