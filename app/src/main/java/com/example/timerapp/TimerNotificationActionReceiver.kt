package com.example.timerapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.timerapp.utils.NotificationUtil
import com.example.timerapp.utils.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            AppConstants.ACTION_STOP -> {
                TimerActivity.removeAlarm(context)
                PrefUtil.setTimerState(TimerActivity.TimerState.Stopped,context)
                NotificationUtil.hideNotification(context)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = TimerActivity.setAlarm(context,TimerActivity.nowSeconds,secondsRemaining)

                PrefUtil.setTimerState(TimerActivity.TimerState.Running,context)
                PrefUtil.setSecondsRemaining(secondsRemaining,context)
                NotificationUtil.showTimerRunning(context,wakeUpTime)

            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = TimerActivity.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining,context)
                TimerActivity.removeAlarm(context)
                PrefUtil.setTimerState(TimerActivity.TimerState.Paused,context)
                NotificationUtil.showTimerPaused(context)

            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = TimerActivity.setAlarm(context,TimerActivity.nowSeconds,secondsRemaining)

                PrefUtil.setTimerState(TimerActivity.TimerState.Running,context)
                NotificationUtil.showTimerRunning(context,wakeUpTime)

            }
        }
    }
}
