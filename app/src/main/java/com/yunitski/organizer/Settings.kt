package com.yunitski.organizer

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class Settings : AppCompatActivity(), View.OnClickListener {

    private lateinit var listBtn: TextView
    private lateinit var columnBtn: TextView
    private lateinit var sharedPreferences: SharedPreferences
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
            listBtn.background = ContextCompat.getDrawable(applicationContext, R.drawable.back_gray)
            columnBtn.background = ContextCompat.getDrawable(applicationContext, R.drawable.back_white)
        } else {
            columnBtn.background = ContextCompat.getDrawable(applicationContext, R.drawable.back_gray)
            listBtn.background = ContextCompat.getDrawable(applicationContext, R.drawable.back_white)
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
            listBtn.background = ContextCompat.getDrawable(applicationContext, R.drawable.back_gray)
            columnBtn.background = ContextCompat.getDrawable(applicationContext, R.drawable.back_white)
            sharedPreferences = getSharedPreferences(MainActivity.LAYOUT_FILE, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(MainActivity.LAYOUT_KEY, MainActivity.ONE_COLUMN_LAYOUT)
            editor.apply()
        } else if (v?.id == R.id.column_set){
            columnBtn.background = ContextCompat.getDrawable(applicationContext, R.drawable.back_gray)
            listBtn.background = ContextCompat.getDrawable(applicationContext, R.drawable.back_white)
            sharedPreferences = getSharedPreferences(MainActivity.LAYOUT_FILE, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(MainActivity.LAYOUT_KEY, MainActivity.TWO_COLUMN_LAYOUT)
            editor.apply()
        }
    }
}