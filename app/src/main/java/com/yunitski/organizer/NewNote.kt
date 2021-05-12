package com.yunitski.organizer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class NewNote : AppCompatActivity(), View.OnClickListener {

    private var title: EditText? = null
    private var message: EditText? = null
    private var backNSave: ImageButton? = null
    private var hideKb: ImageButton? = null
    private var isKbUp: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        title = findViewById(R.id.et_title)
        message = findViewById(R.id.et_message)
        message?.requestFocus()
        showKb()
        isKbUp = true
        backNSave = findViewById(R.id.ib_back_n_save)
        backNSave?.setOnClickListener(this)
        hideKb = findViewById(R.id.ib_hide_kb)
        hideKb?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
    }
    private fun showKb(){
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        isKbUp = true
    }
    private fun hideKb(){
        val imm = getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(message?.windowToken, 0)
        isKbUp = false
    }

    override fun onBackPressed() {
        if (!isKbUp!!)
        super.onBackPressed()
    }
}