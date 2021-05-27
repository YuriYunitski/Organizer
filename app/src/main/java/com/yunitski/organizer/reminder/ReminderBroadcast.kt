package com.yunitski.organizer.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yunitski.organizer.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val builder: NotificationCompat.Builder? = NotificationCompat.Builder(context!!, "ntf").setSmallIcon(
            R.drawable.ic_baseline_check_24)?.setContentTitle("oi")?.setContentText("vei")?.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        manager.notify(200, builder?.build()!!)
    }

    fun getMilliFromDate(dateFormat: String): Long {
        var date: Date? = Date()
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date!!.time
    }
}