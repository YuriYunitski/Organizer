package com.yunitski.organizer.reminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yunitski.organizer.R
import java.util.*

class RemindActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var addRemind: FloatingActionButton
    lateinit var calendar: CalendarView
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<String>
    val chosenDate: String = ""
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
        listView = findViewById(R.id.list_remind)
        addRemind.setOnClickListener(this)
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
        startActivity(Intent(this, NewReminder::class.java))
    }
    private fun getCurrentDate() = "${c.get(Calendar.DAY_OF_MONTH)}.${c.get(Calendar.MONTH) + 1}.${c.get(Calendar.YEAR)}"
}
