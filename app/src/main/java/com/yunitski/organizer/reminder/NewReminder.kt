package com.yunitski.organizer.reminder

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import com.yunitski.organizer.DataBase
import com.yunitski.organizer.R
import java.util.*

class NewReminder : AppCompatActivity(), View.OnClickListener {

    lateinit var closeBtn: ImageButton
    lateinit var nextBtn: ImageButton
    lateinit var rName: EditText
    lateinit var rDesc: EditText
    var isKB: Boolean = false
    private val c: Calendar = GregorianCalendar()
    var chosenDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_reminder)
        closeBtn = findViewById(R.id.ib_close)
        nextBtn = findViewById(R.id.ib_next)
        closeBtn.setOnClickListener(this)
        nextBtn.setOnClickListener(this)
        rName = findViewById(R.id.et_name)
        rDesc = findViewById(R.id.et_desc)
        rName.requestFocus()
        isKB = true
        showKb()
        val i = intent
        chosenDate = i.getStringExtra("chosenDate").toString()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.ib_close){
            this.finish()
            hideKb()
        } else if (v?.id == R.id.ib_next){
            val i = Intent(this, ReminderTime::class.java)
            i.putExtra("name", rName.text.toString())
            i.putExtra("desc", rDesc.text.toString())
            i.putExtra("date", getCurrentDate())
            i.putExtra("time", getCurrentTime())
            i.putExtra("chosenDate", chosenDate)
            startActivityForResult(i, 17)
            if (isKB) {
                hideKb()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isKB) {
            hideKb()
        }
    }

    override fun onResume() {
        super.onResume()
        rName.requestFocus()
        if (!isKB){
            showKb()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            this.finish()
        }
    }
    private fun showKb(){
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        isKB = true
    }
    private fun hideKb(){
        val imm = getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(rName.windowToken, 0)
        isKB = false
    }

    private fun getCurrentDate(): String{
        val c: Calendar = GregorianCalendar()
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
}