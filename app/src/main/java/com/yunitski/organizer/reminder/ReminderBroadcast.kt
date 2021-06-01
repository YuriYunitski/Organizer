package com.yunitski.organizer.reminder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yunitski.organizer.reminder.ReminderTime.Companion.NOTIFICATION_CHANNEL_ID


class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification? = intent!!.getParcelableExtra(NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_MONEY_KEEPER",
                importance
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        notificationManager.notify(id, notification)
    }
    companion object{
        var NOTIFICATION_ID = "notification-id-mk"
        var NOTIFICATION = "notification-mk"
    }
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val builder: NotificationCompat.Builder? = NotificationCompat.Builder(context!!, "ntf").setSmallIcon(
//            R.drawable.ic_baseline_check_24)?.setContentTitle(intent?.getStringExtra("name"))?.setContentText(intent?.getStringExtra("desc"))?.setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        val manager: NotificationManagerCompat = NotificationManagerCompat.from(context)
//        manager.notify(200, builder?.build()!!)
//    }
}