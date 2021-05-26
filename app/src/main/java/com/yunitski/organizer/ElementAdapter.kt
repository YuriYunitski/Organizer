package com.yunitski.organizer

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView


class ElementAdapter(private val context: Context, private val list: MutableList<Element>, private val listen: ElementAdapterListener): RecyclerView.Adapter<ElementAdapter.MyViewHolder>(), Filterable {

    var filteredElementList: MutableList<Element> = mutableListOf()
    var selectedElementList: MutableList<Element> = mutableListOf()
    private var multiSelect: Boolean = false
    init {
        for (i in list){
            filteredElementList.add(i)
        }
    }

    private var actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            multiSelect = true
            mode?.menuInflater?.inflate(R.menu.action_mode_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            for (i in selectedElementList){
        val myDBHandler = DataBase(context, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDBHandler.writableDatabase
        var tit = ""
        var ms = ""
        var dt = ""
        var tm = ""
        var id = ""
        val c: Cursor = db.rawQuery("SELECT ${DataBase.COLUMN_TITLE}, ${DataBase.COLUMN_MESSAGE}, ${DataBase.COLUMN_DATE}, ${DataBase.COLUMN_TIME} FROM ${DataBase.TABLE_NOTES} WHERE ${DataBase.COLUMN_ID} = '${i.id}'", null)
        while (c.moveToNext()){
            tit = c.getString(c.getColumnIndex(DataBase.COLUMN_TITLE))
            ms = c.getString(c.getColumnIndex(DataBase.COLUMN_MESSAGE))
            dt = c.getString(c.getColumnIndex(DataBase.COLUMN_DATE))
            tm = c.getString(c.getColumnIndex(DataBase.COLUMN_TIME))
            id = i.id
        }
        c.close()
        val cv = ContentValues()
        cv.put(DataBase.COLUMN_TITLE, tit)
        cv.put(DataBase.COLUMN_MESSAGE, ms)
        cv.put(DataBase.COLUMN_DATE, dt)
        cv.put(DataBase.COLUMN_TIME, tm)
        cv.put(DataBase.COLUMN_ID, id)
        db.insert(DataBase.TABLE_DELETED, null, cv)
        db.delete(DataBase.TABLE_NOTES, DataBase.COLUMN_ID + "=?", arrayOf(i.id))
        db.close()
                filteredElementList.remove(i)
            }
            mode?.finish()
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            multiSelect = false
            selectedElementList.clear()
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementAdapter.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElementAdapter.MyViewHolder, position: Int) {
        holder.update(filteredElementList[position])
    }




    override fun getItemCount() = filteredElementList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleV: TextView = itemView.findViewById(R.id.title_item)
        private val messV: TextView = itemView.findViewById(R.id.mess_item)
        private val idItem: TextView = itemView.findViewById(R.id.id_item)
        private val dateV: TextView = itemView.findViewById(R.id.date_item)
        private val timeV: TextView = itemView.findViewById(R.id.time_item)
        private val frame: FrameLayout = itemView.findViewById(R.id.frameLayout)
            private fun selectItem(i: Element){
                if (multiSelect){
                    if (selectedElementList.contains(i)){
                        selectedElementList.remove(i)
                        frame.setBackgroundColor(Color.WHITE)
                    } else {
                        selectedElementList.add(i)
                        frame.setBackgroundColor(Color.LTGRAY)
                    }
                }
            }

            fun update(el: Element){
                titleV.text = el.title
                messV.text = el.message
                idItem.text = el.id
                dateV.text = el.date
                timeV.text = el.time
                if(selectedElementList.contains(el)){
                    frame.setBackgroundColor(Color.LTGRAY)
                } else {
                    frame.setBackgroundColor(Color.WHITE)
                }
                itemView.setOnLongClickListener { v ->
                    (v?.context as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
                    selectItem(el)
                    true
                }
                itemView.setOnClickListener {
                    if (multiSelect) {
                        selectItem(el)
                    } else {
                        listen.onElementSelected(filteredElementList[adapterPosition])
                    }
                }
            }
        }
        interface ElementAdapterListener {
        fun onElementSelected(elts: Element?)
    }

    override fun getFilter(): Filter {
        return elementFilter
    }

    private val elementFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            filteredElementList.clear()
            if (constraint == null || constraint.isEmpty()){
                filteredElementList.addAll(list)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in list){
                    if (item.title.lowercase().contains(filterPattern)){
                        filteredElementList.add(item)
                    }
                }
            }
            val res = FilterResults()
            res.values = filteredElementList
            return res
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredElementList = results?.values as MutableList<Element>
            notifyDataSetChanged()
        }
    }

        fun clear() {
        val size: Int = list.size
        if (size > 0) {
            for (i in 0 until size) {
                list.removeAt(0)
            }
            notifyItemRangeRemoved(0, size)
        }
    }
}