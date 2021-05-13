package com.yunitski.organizer

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton

class EditNote : AppCompatActivity(), View.OnClickListener {

    private lateinit var title: EditText
    private lateinit var message: EditText
    private lateinit var backNSave: ImageButton

    private var ttl: String? = null
    private var msg: String? = null
    private var id: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        title = findViewById(R.id.et_title)
        message = findViewById(R.id.et_message)
        message.requestFocus()
        ttl = intent.getStringExtra("ttl")
        msg = intent.getStringExtra("msg")
        id = intent.getStringExtra("id")
        title.setText(ttl)
        message.setText(msg)
        backNSave = findViewById(R.id.ib_back_n_save)
        backNSave.setOnClickListener(this)
        showKb()
    }

    override fun onClick(v: View?) {
        val myDBHandler = MyDBHandler(this, "notesDB.db", null, 1 )
        val db: SQLiteDatabase = myDBHandler.writableDatabase
        val strSQL = "UPDATE ${MyDBHandler.TABLE_NOTES} SET ${MyDBHandler.COLUMN_TITLE} = '${title.text}', ${MyDBHandler.COLUMN_MESSAGE} = '${message.text}' WHERE ${MyDBHandler.COLUMN_ID} = $id;"
        db.execSQL(strSQL)
        db.close()
        hideKb()
        finish()
    }
    private fun showKb(){
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
    private fun hideKb(){
        val imm = getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(message.windowToken, 0)
    }
}