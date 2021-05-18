package com.yunitski.organizer

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener, ElementAdapter.ElementAdapterListener {

    private var toolBar: Toolbar? = null
    private var fab: FloatingActionButton? = null
    private lateinit var adapter: ElementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        updateUi()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem: MenuItem = menu?.findItem(R.id.search_icon)!!
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Текст для поиска"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
        updateUi()
        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position: Int = adapter.getPosition()
        val myDBHandler = MyDBHandler(this, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDBHandler.writableDatabase
        db.delete(MyDBHandler.TABLE_NOTES, MyDBHandler.COLUMN_ID + "=?", arrayOf(position.toString()))
        updateUi()
        return super.onContextItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.remind){
            startActivity(Intent(this, RemindActivity::class.java))
        } else if(id == R.id.settings){
            startActivity(Intent(this, Settings::class.java))
        }
        val drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.floatingActionButton2 ->{
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
        val recyclerView: RecyclerView = findViewById(R.id.recycler_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val messageList: MutableList<String> = mutableListOf()
        val titleList: MutableList<String> = mutableListOf()
        val elements: MutableList<Element> = mutableListOf()
        val idList: MutableList<String> = mutableListOf()
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
        adapter = ElementAdapter(this, elements, this)
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
    }

    override fun onElementSelected(elts: Element?) {
        val intent = Intent(this, EditNote::class.java)
        intent.putExtra("id", elts?.id)
        startActivity(intent)
    }
}