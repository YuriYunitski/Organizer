package com.yunitski.organizer

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Archive : AppCompatActivity(), ElementAdapterArchive.ElementAdapterListenerArchive, View.OnClickListener {
    private lateinit var adapter: ElementAdapterArchive
    private lateinit var deleteBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        val toolBar: Toolbar = findViewById(R.id.tool_bar)
        setSupportActionBar(toolBar)
        title = "Корзина"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        deleteBtn = findViewById(R.id.delete_all)
        deleteBtn.setOnClickListener(this)
        updUI()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position: Int = adapter.getPosition()
        val myDB = DataBase(this, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDB.writableDatabase
        var tit = ""
        var ms = ""
        var dt = ""
        var tm = ""
        var id = ""
        val cursorMain: Cursor = db.rawQuery("SELECT ${DataBase.COLUMN_TITLE}, ${DataBase.COLUMN_MESSAGE}, ${DataBase.COLUMN_DATE}, ${DataBase.COLUMN_TIME} FROM ${DataBase.TABLE_DELETED} WHERE ${DataBase.COLUMN_ID} = '$position'", null)
        while (cursorMain.moveToNext()){
            tit = cursorMain.getString(cursorMain.getColumnIndex(DataBase.COLUMN_TITLE))
            ms = cursorMain.getString(cursorMain.getColumnIndex(DataBase.COLUMN_MESSAGE))
            dt = cursorMain.getString(cursorMain.getColumnIndex(DataBase.COLUMN_DATE))
            tm = cursorMain.getString(cursorMain.getColumnIndex(DataBase.COLUMN_TIME))
            id = position.toString()
        }
        cursorMain.close()
        val cv = ContentValues()
        cv.put(DataBase.COLUMN_TITLE, tit)
        cv.put(DataBase.COLUMN_MESSAGE, ms)
        cv.put(DataBase.COLUMN_DATE, dt)
        cv.put(DataBase.COLUMN_TIME, tm)
        cv.put(DataBase.COLUMN_ID, id)
        db.insert(DataBase.TABLE_NOTES, null, cv)
        db.delete(DataBase.TABLE_DELETED, DataBase.COLUMN_ID + "=?", arrayOf(position.toString()))
        db.close()
        updUI()
        return super.onContextItemSelected(item)
    }

    private fun updUI(){
        val recycler: RecyclerView = findViewById(R.id.recycler_archive)
        recycler.layoutManager = LinearLayoutManager(this)
        val messageList: MutableList<String> = mutableListOf()
        val titleList: MutableList<String> = mutableListOf()
        val elements: MutableList<Element> = mutableListOf()
        val idList: MutableList<String> = mutableListOf()
        val myDBHandlerArchive = DataBase(this, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDBHandlerArchive.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${DataBase.TABLE_DELETED};", null)
        if (cursor.count > 0){
            if (cursor.moveToFirst()){
                do {
                    val idx1: Int = cursor.getColumnIndex(DataBase.COLUMN_ID)
                    val idx2: Int = cursor.getColumnIndex(DataBase.COLUMN_TITLE)
                    val idx3: Int = cursor.getColumnIndex(DataBase.COLUMN_MESSAGE)
                    val idx4: Int = cursor.getColumnIndex(DataBase.COLUMN_DATE)
                    val idx5: Int = cursor.getColumnIndex(DataBase.COLUMN_TIME)

                    if (cursor.getString(idx2).isEmpty()) {
                        idList.add(0, cursor.getString(idx1))
                        val s = cursor.getString(idx3)
                        val sp: List<String> = s.split("\n")
                        titleList.add(0, sp[0])
                        if (sp.size > 1) {
                            messageList.add(0, sp[1])
                            elements.add(0, Element(sp[0], sp[1], cursor.getString(idx1), cursor.getString(idx4), cursor.getString(idx5)))
                        } else {
                            elements.add(0, Element(sp[0], "", cursor.getString(idx1), cursor.getString(idx4), cursor.getString(idx5)))
                        }
                    } else {
                        idList.add(0, cursor.getString(idx1))
                        titleList.add(0, cursor.getString(idx2))
                        messageList.add(0, cursor.getString(idx3))
                        elements.add(0, Element(cursor.getString(idx2), cursor.getString(idx3), cursor.getString(idx1), cursor.getString(idx4), cursor.getString(idx5)))
                    }
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        adapter = ElementAdapterArchive(this, elements, this)
        recycler.adapter = adapter
        recycler.addOnItemTouchListener(
            ElementTouchListener(
                applicationContext,
                object : ElementTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        Toast.makeText(
                            applicationContext,
                            "You clicked " + idList[position],
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        )
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
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Удалить все заметки?")
        builder.setPositiveButton("ok"
        ) { dialog, which ->
            val dbHandlerArchice =
                DataBase(applicationContext, "notesDB.db", null, 1)
            val db: SQLiteDatabase = dbHandlerArchice.writableDatabase
            db.delete(DataBase.TABLE_DELETED, null, null)
            updUI()
        }
        builder.setNegativeButton("cancel"
        ) { dialog, which -> }
        val dialog = builder.create()
        dialog.show()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#1F2B39")))
    }

    override fun onElementSelected(elts: Element?) {
        //Toast.makeText(this, "Используйте контекстное меню для восстановления", Toast.LENGTH_SHORT).show()
    }
}
