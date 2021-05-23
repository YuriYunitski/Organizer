package com.yunitski.organizer

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class Settings : AppCompatActivity(), View.OnClickListener {

    lateinit var listBtn: TextView
    lateinit var columnBtn: TextView
    lateinit var sharedPreferences: SharedPreferences
    var layout: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolBar: Toolbar = findViewById(R.id.tool_bar)
        setSupportActionBar(toolBar)
        title = "Настройки"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        listBtn = findViewById(R.id.list_set)
        columnBtn = findViewById(R.id.column_set)
        listBtn.setOnClickListener(this)
        columnBtn.setOnClickListener(this)
        sharedPreferences = getSharedPreferences(MainActivity.LAYOUT_FILE, Context.MODE_PRIVATE)
        layout = sharedPreferences.getString(MainActivity.LAYOUT_KEY, MainActivity.ONE_COLUMN_LAYOUT)
        if (layout == MainActivity.ONE_COLUMN_LAYOUT){
            listBtn.setBackground(resources.getDrawable(R.drawable.back_gray))
            columnBtn.setBackground(resources.getDrawable(R.drawable.back_white))
        } else {
            columnBtn.setBackground(resources.getDrawable(R.drawable.back_gray))
            listBtn.setBackground(resources.getDrawable(R.drawable.back_white))
        }
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
        if (v?.id == R.id.list_set){
            listBtn.setBackground(resources.getDrawable(R.drawable.back_gray))
            columnBtn.setBackground(resources.getDrawable(R.drawable.back_white))
            sharedPreferences = getSharedPreferences(MainActivity.LAYOUT_FILE, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(MainActivity.LAYOUT_KEY, MainActivity.ONE_COLUMN_LAYOUT)
            editor.apply()
        } else if (v?.id == R.id.column_set){
            columnBtn.setBackground(resources.getDrawable(R.drawable.back_gray))
            listBtn.setBackground(resources.getDrawable(R.drawable.back_white))
            sharedPreferences = getSharedPreferences(MainActivity.LAYOUT_FILE, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(MainActivity.LAYOUT_KEY, MainActivity.TWO_COLUMN_LAYOUT)
            editor.apply()
        }
    }
}