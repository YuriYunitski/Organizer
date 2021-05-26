package com.yunitski.organizer.reminer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.yunitski.organizer.R

class NewReminder : AppCompatActivity() {

    lateinit var closeBtn: ImageButton
    lateinit var nextBtn: ImageButton
    lateinit var rName: EditText
    lateinit var rDesc: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_reminder)

    }
}