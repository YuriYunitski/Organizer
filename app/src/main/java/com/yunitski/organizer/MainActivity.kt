package com.yunitski.organizer

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    private var toolBar: Toolbar? = null
    private var fab: FloatingActionButton? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ElementAdapter
    private var isAd: Boolean = false
    private lateinit var t: String
    private lateinit var m: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)
        title = "Заметки"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle =
            ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        fab = findViewById(R.id.floatingActionButton2)
        fab?.setOnClickListener(this)



        updateUi()
    }



    override fun onResume() {
        super.onResume()
        updateUi()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onClick(v: View?) {
        val id: Int? = v?.id
        when(id){
            R.id.floatingActionButton2 ->{
                startActivity(Intent(this, NewNote::class.java))
            }
        }
    }

    private fun updateUi() {
        recyclerView = findViewById(R.id.recycler_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val idList: MutableList<String> = mutableListOf()
        val messageList: MutableList<String> = mutableListOf()
        val titleList: MutableList<String> = mutableListOf()
        val elements: MutableList<Element> = mutableListOf()
        val myDBHandler: MyDBHandler = MyDBHandler(this, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDBHandler.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM " + MyDBHandler.TABLE_NOTES + ";", null)
        while (cursor.moveToNext()){
            val idx1: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_ID)
            val idx2: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_TITLE)
            val idx3: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_MESSAGE)
            idList.add(0, cursor.getString(idx1))
            titleList.add(0, cursor.getString(idx2))
            messageList.add(0, cursor.getString(idx3))
            elements.add(0, Element(cursor.getString(idx2), cursor.getString(idx3)))
        }
        db.close()
        cursor.close()


        if (!isAd) {
            adapter = ElementAdapter(elements)
            isAd = true
        } else {
            adapter.clear()
            adapter.addAll(elements)
            adapter.notifyDataSetChanged()
        }
        recyclerView.adapter = adapter
        adapter.onItemClick = { pos ->
            val intent = Intent(this, EditNote::class.java)
            intent.putExtra("ttl", titleList[pos])
            intent.putExtra("msg", messageList[pos])
            intent.putExtra("id", idList[pos])
            startActivity(intent)
        }
    //        listView.adapter = adapter
//        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
//            val actualPosition: Int = titleList.indexOf(adapter.getItem(position))
//            Toast.makeText(this, "" + idList.get(actualPosition) + " " + titleList.get(actualPosition) + " " + messageList.get(actualPosition), Toast.LENGTH_SHORT).show()
//        })
//    }

    }
}