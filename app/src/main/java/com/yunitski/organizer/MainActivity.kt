package com.yunitski.organizer

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    private var toolBar: Toolbar? = null
    private var fab: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<String>? = null
    private var adapter: ArrayAdapter<String>? = null
    private var inputValue: EditText? = null
    private var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)
        title = "Заметки"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        fab = findViewById(R.id.floatingActionButton2)
        fab?.setOnClickListener(this)
        recyclerView = findViewById(R.id.rec_view)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onClick(v: View?) {

        startActivity(Intent(this, NewNote::class.java))


//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        builder.setCancelable(false)
//        builder.setTitle("try")
//        val inflater: LayoutInflater = layoutInflater
//        val view: View = inflater.inflate(R.layout.add_dialog, null)
//        builder.setView(view)
//        inputValue = view.findViewById(R.id.et_add_dialog)
//        builder.setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
//            sharedPreferences = getSharedPreferences("note", Context.MODE_PRIVATE)
//            val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
//            editor?.putString("n", inputValue?.text.toString())
//            editor?.apply()
//        })
//        builder.setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which ->  })
//        val dialog = builder.create()
//        dialog.show()
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#374B63")))
    }

    fun updateUi(){
        list = mutableListOf()

    }


}