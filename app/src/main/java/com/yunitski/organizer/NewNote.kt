package com.yunitski.organizer

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class NewNote : AppCompatActivity(), View.OnClickListener {

    private lateinit var title: EditText

    private lateinit var message: EditText
    private var backNSave: ImageButton? = null
    private lateinit var myDBHandler: MyDBHandler
    private var idList: MutableList<String>? = null
    private var titleList: MutableList<String>? = null
    private var messageList: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        title = findViewById(R.id.et_title)
        message = findViewById(R.id.et_message)
        message.requestFocus()
        showKb()
        backNSave = findViewById(R.id.ib_back_n_save)
        backNSave?.setOnClickListener(this)
        myDBHandler = MyDBHandler(this, "notesDB.db", null, 1)

    }

    override fun onClick(v: View?) {
        val id: Int? = v?.id
        when (id) {
            R.id.ib_back_n_save -> {
                if (!title.text.toString().isEmpty() || !message.text.toString().isEmpty()) {
                    val values = ContentValues()
                    values.put(MyDBHandler.COLUMN_TITLE, title.text.toString())
                    values.put(MyDBHandler.COLUMN_MESSAGE, message.text.toString())
                    val db: SQLiteDatabase = myDBHandler.writableDatabase
                    db.insert(MyDBHandler.TABLE_NOTES, null, values)
                    db.close()
                }
                    hideKb()
                    finish()
            }
        }
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
        imm.hideSoftInputFromWindow(message?.windowToken, 0)
    }
//    private fun save(){
//        val db: SQLiteDatabase? = dbHelper?.writableDatabase
//        val cursor: Cursor? = db?.query(
//            InputData.TaskEntry.TABLE,
//            arrayOf<String>(
//                InputData.TaskEntry.ID,
//                InputData.TaskEntry.TITLE,
//                InputData.TaskEntry.MESSAGE
//            ),
//            null,
//            null,
//            null,
//            null,
//            null
//        )
//        while (cursor?.moveToNext() == true){
//            val indexID: Int =cursor.getColumnIndex(InputData.TaskEntry.ID)
//            val indexTITLE: Int = cursor.getColumnIndex(InputData.TaskEntry.TITLE)
//            val indexMESSAGE: Int = cursor.getColumnIndex(InputData.TaskEntry.MESSAGE)
//            idList?.add(cursor.getString(indexID))
//            titleList?.add(cursor.getString(indexTITLE))
//            messageList?.add(cursor.getString(indexMESSAGE))
//        }
//        cursor?.close()
//        db?.close()
//    }
}