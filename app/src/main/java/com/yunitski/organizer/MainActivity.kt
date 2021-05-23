package com.yunitski.organizer

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener, ElementAdapter.ElementAdapterListener {

    private var toolBar: Toolbar? = null
    private var fab: FloatingActionButton? = null
    private lateinit var adapter: ElementAdapter
    private lateinit var adapterTwo: ElementAdapter
    lateinit var sharedPreferences: SharedPreferences
    var layout: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(Companion.LAYOUT_FILE, Context.MODE_PRIVATE)
        layout = sharedPreferences.getString(LAYOUT_KEY, ONE_COLUMN_LAYOUT)
        if (layout == ONE_COLUMN_LAYOUT) {
            setContentView(R.layout.activity_main)
            toolBar = findViewById(R.id.tool_bar)
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
        } else {
            setContentView(R.layout.activity_main_two_columns)
            toolBar = findViewById(R.id.tool_bar_tc)
            setSupportActionBar(toolBar)
            title = "Заметки"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_two_columns)
            val toggle =
                ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            val navigationView = findViewById<NavigationView>(R.id.nav_view_tc)
            navigationView.setNavigationItemSelectedListener(this)
            fab = findViewById(R.id.floatingActionButton_tc)
            fab?.setOnClickListener(this)
        }
        updateUi()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem: MenuItem = menu?.findItem(R.id.search_icon)!!
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Текст для поиска"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (layout == ONE_COLUMN_LAYOUT){
                    adapter.filter.filter(query)
                } else{
                    adapterTwo.filter.filter(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if (layout == ONE_COLUMN_LAYOUT){
                    adapter.filter.filter(newText)
                } else{
                    adapterTwo.filter.filter(newText)
                }
                return false
            }

        })
        updateUi()
        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var position = 0
        if (layout == ONE_COLUMN_LAYOUT){
            position = adapter.getPosition()
        } else {
            position = adapterTwo.getPosition()
        }
        val myDBHandler = MyDBHandler(this, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDBHandler.writableDatabase
        val myDBHandlerArchive = MyDBHandlerArchive(this, "notesDBarchive.db", null, 1)
        val aDb: SQLiteDatabase = myDBHandlerArchive.writableDatabase
        var tit = ""
        var ms = ""
        var dt = ""
        var tm = ""
        var id = ""
        val c: Cursor = db.rawQuery("SELECT ${MyDBHandler.COLUMN_TITLE}, ${MyDBHandler.COLUMN_MESSAGE}, ${MyDBHandler.COLUMN_DATE}, ${MyDBHandler.COLUMN_TIME} FROM ${MyDBHandler.TABLE_NOTES} WHERE ${MyDBHandler.COLUMN_ID} = '$position'", null)
        while (c.moveToNext()){
            tit = c.getString(c.getColumnIndex(MyDBHandler.COLUMN_TITLE))
            ms = c.getString(c.getColumnIndex(MyDBHandler.COLUMN_MESSAGE))
            dt = c.getString(c.getColumnIndex(MyDBHandler.COLUMN_DATE))
            tm = c.getString(c.getColumnIndex(MyDBHandler.COLUMN_TIME))
            id = position.toString()
        }
        c.close()
        val cv = ContentValues();
        cv.put(MyDBHandlerArchive.COLUMN_TITLE, tit)
        cv.put(MyDBHandlerArchive.COLUMN_MESSAGE, ms)
        cv.put(MyDBHandlerArchive.COLUMN_DATE, dt)
        cv.put(MyDBHandlerArchive.COLUMN_TIME, tm)
        cv.put(MyDBHandlerArchive.COLUMN_ID, id)
        aDb.insert(MyDBHandlerArchive.TABLE_NOTES, null, cv)
        db.delete(MyDBHandler.TABLE_NOTES, MyDBHandler.COLUMN_ID + "=?", arrayOf(position.toString()))
        aDb.close()
        db.close()


        updateUi()
        return super.onContextItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = getSharedPreferences(Companion.LAYOUT_FILE, Context.MODE_PRIVATE)
        layout = sharedPreferences.getString(LAYOUT_KEY, ONE_COLUMN_LAYOUT)
        if (layout == ONE_COLUMN_LAYOUT) {
            setContentView(R.layout.activity_main)
            toolBar = findViewById(R.id.tool_bar)
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
        } else {
            setContentView(R.layout.activity_main_two_columns)
            toolBar = findViewById(R.id.tool_bar_tc)
            setSupportActionBar(toolBar)
            title = "Заметки"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_two_columns)
            val toggle =
                ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            val navigationView = findViewById<NavigationView>(R.id.nav_view_tc)
            navigationView.setNavigationItemSelectedListener(this)
            fab = findViewById(R.id.floatingActionButton_tc)
            fab?.setOnClickListener(this)
        }
        updateUi()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
//        if (id == R.id.remind){
//            startActivity(Intent(this, RemindActivity::class.java))
//        } else
            if(id == R.id.settings){
            startActivity(Intent(this, Settings::class.java))
        } else if(id == R.id.archive){
            startActivity(Intent(this, Archive::class.java))
        }
        if(layout == ONE_COLUMN_LAYOUT) {
            val drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val drawerLayout = findViewById<View>(R.id.drawer_layout_two_columns) as DrawerLayout
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.floatingActionButton2, R.id.floatingActionButton_tc ->{
                newNote()
            }
        }
    }

    private fun newNote(){
        startActivityForResult(Intent(this, NewNote::class.java), 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000){
            updateUi()
        }
    }

    private fun updateUi() {
        val messageList: MutableList<String> = mutableListOf()
        val titleList: MutableList<String> = mutableListOf()
        val idList: MutableList<String> = mutableListOf()
        val elements: MutableList<Element> = mutableListOf()
        val myDBHandler = MyDBHandler(this, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDBHandler.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM " + MyDBHandler.TABLE_NOTES + ";", null)
        while (cursor.moveToNext()) {
            val idx1: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_ID)
            val idx2: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_TITLE)
            val idx3: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_MESSAGE)
            val idx4: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_DATE)
            val idx5: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_TIME)

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
        }
        db.close()
        cursor.close()
        val recyclerView: RecyclerView
        val recyclerViewTC: RecyclerView
        if (layout == ONE_COLUMN_LAYOUT){
            adapter = ElementAdapter(this, elements, this)
            recyclerView= findViewById(R.id.recycler_list)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            recyclerView.addOnItemTouchListener(
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
        } else if (layout == TWO_COLUMN_LAYOUT){
            adapterTwo = ElementAdapter(this, elements, this)
            recyclerViewTC = findViewById(R.id.recycler_tc)
            recyclerViewTC.layoutManager = GridLayoutManager(this, 2)
            recyclerViewTC.adapter = adapterTwo
            recyclerViewTC.addOnItemTouchListener(
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
    }

    override fun onElementSelected(elts: Element?) {
        val intent = Intent(this, EditNote::class.java)
        intent.putExtra("id", elts?.id)
        startActivity(intent)
    }

    companion object {
        const val LAYOUT_FILE: String = "layoutFile"
        const val ONE_COLUMN_LAYOUT = "oneColumnLayout"
        const val TWO_COLUMN_LAYOUT = "twoColumnLayout"
        const val LAYOUT_KEY = "1664"
    }
}