package com.yunitski.organizer

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener, ElementAdapter.ElementAdapterListener {

    private var toolBar: Toolbar? = null
    private var fab: FloatingActionButton? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ElementAdapter
    private var isAd: Boolean = false
    val idList: MutableList<String> = mutableListOf()

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
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.getFilter().filter(newText)
                return true
            }

        })
        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position: Int = adapter.getPosition()
        val p: String = idList[position]
        deleteFromBase(p)
        updateUi()
        return super.onContextItemSelected(item)
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
        val messageList: MutableList<String> = mutableListOf()
        val titleList: MutableList<String> = mutableListOf()
        val elements: MutableList<Element> = mutableListOf()
        val myDBHandler = MyDBHandler(this, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDBHandler.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM " + MyDBHandler.TABLE_NOTES + ";", null)
        while (cursor.moveToNext()) {
            val idx1: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_ID)
            val idx2: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_TITLE)
            val idx3: Int = cursor.getColumnIndex(MyDBHandler.COLUMN_MESSAGE)
            if (cursor.getString(idx2).isEmpty()) {
                idList.add(0, cursor.getString(idx1))
                val s = cursor.getString(idx3)
                val sp: List<String> = s.split("\n")
                titleList.add(0, sp[0])
                if (sp.size > 1) {
                    messageList.add(0, sp[1])
                    elements.add(0, Element(sp[0], sp[1]))
                } else {
                    elements.add(0, Element(sp[0], ""))
                }
            } else {
                idList.add(0, cursor.getString(idx1))
                titleList.add(0, cursor.getString(idx2))
                messageList.add(0, cursor.getString(idx3))
                elements.add(0, Element(cursor.getString(idx2), cursor.getString(idx3)))
            }
        }

        db.close()
        cursor.close()

        if (!isAd) {
            adapter = ElementAdapter(this, elements, this)
            isAd = true
        } else {
            adapter.clear()
            adapter.addAll(elements)
            adapter.notifyDataSetChanged()
        }

        recyclerView.adapter = adapter
        //recyclerView.addOnItemTouchListener(ElementAdapter.ElementAdapterListener(application, ElementAdapter.ElementAdapterListener.onElementSelected()))
//        adapter.onItemClick = { pos ->
//            //val ap: Int = elements.indexOf(adapter.getItem(pos))
//            val intent = Intent(this, EditNote::class.java)
//            intent.putExtra("id", idList[pos])
//            startActivity(intent)
//        }
    //        listView.adapter = adapter
//        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
//            val actualPosition: Int = titleList.indexOf(adapter.getItem(position))
//            Toast.makeText(this, "" + idList.get(actualPosition) + " " + titleList.get(actualPosition) + " " + messageList.get(actualPosition), Toast.LENGTH_SHORT).show()
//        })
//    }
    }
    private fun deleteFromBase(id: String){
        val myDBHandler = MyDBHandler(this, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDBHandler.writableDatabase
        db.delete(MyDBHandler.TABLE_NOTES, MyDBHandler.COLUMN_ID + "=?", arrayOf(id))
        updateUi()
    }

    override fun onElementSelected(elts: Element?) {

    }
}