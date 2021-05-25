package com.yunitski.organizer

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.view.SupportActionModeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout


class ElementAdapter(private val context: Context, private val list: MutableList<Element>, private val listen: ElementAdapterListener): RecyclerView.Adapter<ElementAdapter.MyViewHolder>(), Filterable {

    var filteredElementList: MutableList<Element> = mutableListOf()
    var selectedElementList: MutableList<Element> = mutableListOf()
    private var multiSelect: Boolean = false
    init {
//        filteredElementList.addAll(list)
        for (i in list){
            filteredElementList.add(i)
        }
    }
    private var actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            multiSelect = true
            menu?.add("Delete")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            for (i in selectedElementList){
//                val myDBHandler: MyDBHandler = MyDBHandler(context, "notesDB.db", null, 1)
//                val db: SQLiteDatabase = myDBHandler.writableDatabase
//                db.delete(MyDBHandler.TABLE_NOTES, MyDBHandler.COLUMN_ID + " = " + i.id, null)
//                db.close()
                //        val myDBHandler = MyDBHandler(this, "notesDB.db", null, 1)
        val myDBHandler: MyDBHandler = MyDBHandler(context, "notesDB.db", null, 1)
        val db: SQLiteDatabase = myDBHandler.writableDatabase
        val myDBHandlerArchive = MyDBHandlerArchive(context, "notesDBarchive.db", null, 1)
        val aDb: SQLiteDatabase = myDBHandlerArchive.writableDatabase
        var tit = ""
        var ms = ""
        var dt = ""
        var tm = ""
        var id = ""
        val c: Cursor = db.rawQuery("SELECT ${MyDBHandler.COLUMN_TITLE}, ${MyDBHandler.COLUMN_MESSAGE}, ${MyDBHandler.COLUMN_DATE}, ${MyDBHandler.COLUMN_TIME} FROM ${MyDBHandler.TABLE_NOTES} WHERE ${MyDBHandler.COLUMN_ID} = '${i.id}'", null)
        while (c.moveToNext()){
            tit = c.getString(c.getColumnIndex(MyDBHandler.COLUMN_TITLE))
            ms = c.getString(c.getColumnIndex(MyDBHandler.COLUMN_MESSAGE))
            dt = c.getString(c.getColumnIndex(MyDBHandler.COLUMN_DATE))
            tm = c.getString(c.getColumnIndex(MyDBHandler.COLUMN_TIME))
            id = i.id
        }
        c.close()
        val cv = ContentValues();
        cv.put(MyDBHandlerArchive.COLUMN_TITLE, tit)
        cv.put(MyDBHandlerArchive.COLUMN_MESSAGE, ms)
        cv.put(MyDBHandlerArchive.COLUMN_DATE, dt)
        cv.put(MyDBHandlerArchive.COLUMN_TIME, tm)
        cv.put(MyDBHandlerArchive.COLUMN_ID, id)
        aDb.insert(MyDBHandlerArchive.TABLE_NOTES, null, cv)
        db.delete(MyDBHandler.TABLE_NOTES, MyDBHandler.COLUMN_ID + "=?", arrayOf(i.id))
        aDb.close()
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
//        val element: Element = filteredElementList[position]
//        holder.titleV.text = element.title
//        holder.messV.text = element.message
//        holder.idItem.text = element.id
//        holder.dateV.text = element.date
//        holder.timeV.text = element.time
////        holder.linL.setOnLongClickListener {
////            setPosition(element.id.toInt())
////            false
////        }
//        if(selectedElementList.contains(element)){
//            holder.frame.setBackgroundColor(Color.LTGRAY);
//        } else {
//            holder.frame.setBackgroundColor(Color.WHITE)
//        }
        holder.update(filteredElementList[position])
//                val element: Element = filteredElementList[position]
//                holder.linL.setOnLongClickListener {
//            setPosition(element.id.toInt())
//            false
//        }
    }




    override fun getItemCount() = filteredElementList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleV: TextView = itemView.findViewById(R.id.title_item)
        val messV: TextView = itemView.findViewById(R.id.mess_item)
        val idItem: TextView = itemView.findViewById(R.id.id_item)
        val linL: LinearLayout = itemView.findViewById(R.id.items_list)
        val dateV: TextView = itemView.findViewById(R.id.date_item)
        val timeV: TextView = itemView.findViewById(R.id.time_item)
        val frame: FrameLayout = itemView.findViewById(R.id.frameLayout)

            //itemView.setOnCreateContextMenuListener(this)
            fun selectItem(i: Element){
                if (multiSelect){
                    if (selectedElementList.contains(i)){
                        selectedElementList.remove(i)
                        frame.setBackgroundColor(Color.WHITE)
                    } else {
                        selectedElementList.add(i)
                        frame.setBackgroundColor(Color.LTGRAY);
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
                    frame.setBackgroundColor(Color.LTGRAY);
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
                        selectItem(el);
                    } else {
                        listen.onElementSelected(filteredElementList[adapterPosition])
                    }
                }
            }
        }

//        override fun onCreateContextMenu(
//            menu: ContextMenu?,
//            v: View?,
//            menuInfo: ContextMenu.ContextMenuInfo?
//        ) {
//            menu!!.add(0, 1, adapterPosition, "action 1")
//            //установка бэкграунда контекстного меню
//            val positionOfMenuItem = 0
//            val item = menu.getItem(positionOfMenuItem)
//            val s = SpannableString("Удалить")
//            s.setSpan(ForegroundColorSpan(Color.BLACK), 0, s.length, 0)
//            item.title = s
//        }

        interface ElementAdapterListener {
        fun onElementSelected(elts: Element?)
    }

    override fun getFilter(): Filter {
        return elementFilter
    }

    private var position = 0

    fun getPosition(): Int {
        return position
    }

    private fun setPosition(position: Int) {
        this.position = position
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

//    override fun onViewRecycled(holder: MyViewHolder) {
//        holder.itemView.setOnLongClickListener(null)
//        super.onViewRecycled(holder)
//    }
}