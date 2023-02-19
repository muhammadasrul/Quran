package com.acun.quranicplus.alarm_shceduler

import com.acun.quranicplus.data.remote.response.prayer.model.Prayer

interface AlarmScheduler {
    fun scheduler(item: Prayer)
    fun cancel(item: Prayer)
}