package com.yunitski.organizer.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TimePicker
import android.widget.Toast
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

            val intent = Intent(this, ReminderBroadcast::class.java)
            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
            val alarm: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val set: String = "$cd $timeChoise"
            val setTime: Long = getMilliFromDate(set)
            val get: String = "$dat $t"
            val getTime: Long = getMilliFromDate(get)
            alarm.set(AlarmManager.RTC_WAKEUP, setTime - getTime, pendingIntent)
            Toast.makeText(this, "${setTime - getTime}", Toast.LENGTH_LONG).show()
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
}