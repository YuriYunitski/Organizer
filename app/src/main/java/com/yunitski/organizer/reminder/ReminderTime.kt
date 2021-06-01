package com.yunitski.organizer.reminder

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.ImageButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.yunitski.organizer.DataBase
import com.yunitski.organizer.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ReminderTime : AppCompatActivity(), View.OnClickListener {

    private lateinit var back: ImageButton
    private lateinit var apply: ImageButton
    private lateinit var timePicker: TimePicker
    private var timeChoise: String = ""
    private var n: String = ""
    private var d: String = ""
    private var dat: String = ""
    private var t: String = ""
    private var cd: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_time)
        back = findViewById(R.id.r_time_back)
        apply = findViewById(R.id.apply)
        back.setOnClickListener(this)
        apply.setOnClickListener(this)
        timePicker = findViewById(R.id.time_picker)
        timePicker.setIs24HourView(true)
        timeChoise = getCurrentTime()
        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            var mm: String = minute.toString()
            if (minute < 10){
                mm = "0$minute"
            }
            timeChoise = "$hourOfDay:$mm"
        }

        val i = intent
        n = i.getStringExtra("name").toString()
        d = i.getStringExtra("desc").toString()
        dat = i.getStringExtra("date").toString()
        t = i.getStringExtra("time").toString()
        cd = i.getStringExtra("chosenDate").toString()
    }

    private fun getCurrentTime(): String{
        val c: Calendar = GregorianCalendar()
        val m = c.get(Calendar.MINUTE)
        var mm: String = m.toString()
        if (m < 10){
            mm = "0$m"
        }
        return "${c.get(Calendar.HOUR_OF_DAY)}:$mm"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        if (v?.id == R.id.r_time_back){
            this.finish()
        } else if (v?.id == R.id.apply){
            val i = Intent()
            setResult(RESULT_OK, i)
            val myDB = DataBase(this, "notesDB.db", null, 1)
            val db: SQLiteDatabase = myDB.writableDatabase
            val values = ContentValues()
            values.put(DataBase.COLUMN_TITLE, n)
            values.put(DataBase.COLUMN_MESSAGE, d)
            values.put(DataBase.COLUMN_DATE, dat)
            values.put(DataBase.COLUMN_TIME, t)
            values.put(DataBase.COLUMN_DATE_CHOSEN, cd)
            values.put(DataBase.COLUMN_TIME_CHOSEN, timeChoise)
            db.insert(DataBase.TABLE_REMINDS, null, values)
            db.close()

//            val db1: SQLiteDatabase = myDB.readableDatabase
//            var idd: String = "0"
//            val cursor: Cursor = db1.rawQuery("SELECT ${DataBase.COLUMN_ID} FROM ${DataBase.TABLE_REMINDS} WHERE ${DataBase.COLUMN_TITLE} = '$n' AND ${DataBase.COLUMN_MESSAGE} = '$d' AND ${DataBase.COLUMN_DATE_CHOSEN} = '$cd' AND ${DataBase.COLUMN_TIME_CHOSEN} = '$timeChoise'", null)
//            while (cursor.moveToNext()) {
//                idd = cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_ID))
//            }
//            cursor.close()
//            db1.close()
//            val intent = Intent(this, ReminderBroadcast::class.java)
//            intent.putExtra("name", n)
//            intent.putExtra("desc", d)
//            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, idd.toInt(), intent, 0)
//            val alarm: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val ss: List<String> = dat.split(".")
            val s = ss[1].toInt()
            val datt = "${ss[0]}.${s - 1}.${ss[2]}"
            val pp: List<String> = cd.split(".")
            val p = pp[1].toInt()
            val cdd = "${pp[0]}.${p - 1}.${pp[2]}"
            val set: String = "$cdd $timeChoise"
            val setTime: Long = getMilliFromDate(set)
            val get: String = "$datt $t"
            val getTime: Long = getMilliFromDate(get)
            val ttt: Long = setTime - SystemClock.elapsedRealtime()
//            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 10000, pendingIntent)
//            Toast.makeText(this, "${setTime - getTime}", Toast.LENGTH_LONG).show()
            scheduleNotification(getNotification(n), ttt)
            this.finish()

        }
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
    companion object{
        const val NOTIFICATION_CHANNEL_ID = "100011664"
        private const val default_notification_channel_id = "default-mk"
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(notification: Notification?, delay: Long){
        val myDB = DataBase(this, "notesDB.db", null, 1)
        val db1: SQLiteDatabase = myDB.readableDatabase
        var idd: String = "0"
        val cursor: Cursor = db1.rawQuery("SELECT ${DataBase.COLUMN_ID} FROM ${DataBase.TABLE_REMINDS} WHERE ${DataBase.COLUMN_TITLE} = '$n' AND ${DataBase.COLUMN_MESSAGE} = '$d' AND ${DataBase.COLUMN_DATE_CHOSEN} = '$cd' AND ${DataBase.COLUMN_TIME_CHOSEN} = '$timeChoise'", null)
        while (cursor.moveToNext()) {
            idd = cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_ID))
        }
        cursor.close()
        db1.close()
        val notificationIntent: Intent = Intent(this, ReminderBroadcast::class.java)
        notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION_ID, idd)
        notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION, notification)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, idd.toInt(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val futureInMillis: Long = SystemClock.elapsedRealtime() + delay
        val alarmManager: AlarmManager = (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
//        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
    }
    private fun getNotification(content: String): Notification? {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, default_notification_channel_id)
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setAutoCancel(true)
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }
}