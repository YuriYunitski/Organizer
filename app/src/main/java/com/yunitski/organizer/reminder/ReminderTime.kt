package com.yunitski.organizer.reminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.yunitski.organizer.R

class ReminderTime : AppCompatActivity(), View.OnClickListener {

    lateinit var back: ImageButton
    lateinit var apply: ImageButton
    lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_time)
        back = findViewById(R.id.r_time_back)
        apply = findViewById(R.id.apply)
        back.setOnClickListener(this)
        apply.setOnClickListener(this)
        text = findViewById(R.id.textViews)

        val i = intent
        val n = i.getStringExtra("name")
        val d = i.getStringExtra("desc")
        val dat = i.getStringExtra("date")
        val t = i.getStringExtra("time")
        text.text = "" + n + d + dat + t
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.r_time_back){
            this.finish()
        } else if (v?.id == R.id.apply){
            val i = Intent()
            setResult(RESULT_OK, i)
            this.finish()

        }
    }
}