package com.yunitski.organizer.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yunitski.organizer.DataBase
import com.yunitski.organizer.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class RemindActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var addRemind: FloatingActionButton
    lateinit var calendar: CalendarView
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<String>
    var chosenDate: String = ""
    private val c: Calendar = GregorianCalendar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remind)
        val toolBar: Toolbar = findViewById(R.id.tool_bar)
        setSupportActionBar(toolBar)
        title = "Напоминания"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        addRemind = findViewById(R.id.add_remind)
        calendar = findViewById(R.id.calendarViewRemind)
        addRemind.setOnClickListener(this)
        chosenDate = getCurrentDate()
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            updUI()
            var mm: String = (month + 1).toString()
            if (month + 1 < 10) {
                mm = "0${month + 1}"
            }
            chosenDate = "$dayOfMonth.$mm.$year"
            updUI()
        }
        updUI()
    }

    override fun onStart() {
        super.onStart()
        updUI()
    }

    override fun onResume() {
        super.onResume()
        updUI()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                this.finish()
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        val i = Intent(this, NewReminder::class.java)
        i.putExtra("chosenDate", chosenDate)
        startActivity(i)
    }
    private fun updUI(){
        val list: MutableList<String> = mutableListOf()
        var names: MutableList<String> = mutableListOf()
        var times: MutableList<String> = mutableListOf()
        val myDb = DataBase(this, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDb.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT ${DataBase.COLUMN_TITLE}, ${DataBase.COLUMN_TIME_CHOSEN}, ${DataBase.COLUMN_DATE_CHOSEN} FROM ${DataBase.TABLE_REMINDS} WHERE ${DataBase.COLUMN_DATE_CHOSEN} = '$chosenDate'", null)
        while (cursor.moveToNext()) {
            list.add("${cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_TIME_CHOSEN))}    ${cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_TITLE))}")
            val d: String = cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_DATE_CHOSEN))
            val t: String = cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_TIME_CHOSEN))
            val i = "$d $t"
            val ii = "${getCurrentDate()} ${getCurrentTime()}"
            times.add("${getMilliFromDate(i)} - ${getMilliFromDate(ii)}")
            //times.add("${getMilliFromTime(cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_TIME_CHOSEN)))} - ${cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_TIME_CHOSEN))}")
        }
        cursor.close()
        db.close()
        listView = findViewById(R.id.list_remind)
        adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val item = super.getView(position, convertView, parent) as TextView
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
                item.setTextColor(Color.BLACK)
                return item
            }
        }
        listView.adapter = adapter
    }
    private fun getCurrentDate(): String{
        val y = c[Calendar.YEAR]
        val m = c[Calendar.MONTH] + 1
        val d = c[Calendar.DAY_OF_MONTH]
        var mm: String = m.toString()
        if (m < 10){
            mm = "0$m"
        }
        return "$d.$mm.$y"
    }

    private fun getCurrentTime(): String{
        val m = c.get(Calendar.MINUTE)
        var mm: String = m.toString()
        if (m < 10){
            mm = "0$m"
        }
        return "${c.get(Calendar.HOUR_OF_DAY)}:$mm"
    }

    fun getMilliFromDate(dateFormat: String): Long {
        var date: Date? = Date()
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date!!.time
    }
    fun getMilliFromTime(time: String): Long{
        val s: List<String> = time.split(":")
        val s1 = s[0]
        val s2 = s[1]
        val ss: Long = 1000 * 60 * s2.toLong()
        val ss2: Long = 1000 * 60 * 60 * s1.toLong()
        return ss + ss2
    }


}
