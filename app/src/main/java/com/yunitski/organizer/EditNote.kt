package com.yunitski.organizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton

class EditNote : AppCompatActivity() {

    private lateinit var title: EditText
    private lateinit var message: EditText
    private var backNSave: ImageButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        title = findViewById(R.id.et_title)
        message = findViewById(R.id.et_message)
        message.requestFocus()
        val ttl: String? = intent.getStringExtra("ttl")
        val msg: String? = intent.getStringExtra("msg")
        title.setText(ttl)
        message.setText(msg)
    }
}