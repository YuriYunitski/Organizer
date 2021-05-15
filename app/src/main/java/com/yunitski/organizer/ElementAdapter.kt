package com.yunitski.organizer

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class ElementAdapter(private val context: Context, private val list: MutableList<Element>, private val listen: ElementAdapterListener): RecyclerView.Adapter<ElementAdapter.MyViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null
    private var position = 0

    private fun setPosition(position: Int) {
        this.position = position
    }
    fun getPosition(): Int {
        return position
    }
    var listFull: MutableList<Element> = mutableListOf()
    lateinit var filteredElementList: MutableList<Element>
    init {
        for (i in list){
            filteredElementList.addAll(listOf(i))
        }

    }

    fun getItem(pos: Int): Element = list[pos]

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        var titleV: TextView? = null
        var messV: TextView? = null
        var linLay: LinearLayout? = null

        init {
            titleV = itemView.findViewById(R.id.title_item)
            messV = itemView.findViewById(R.id.mess_item)
            linLay = itemView.findViewById(R.id.items_list)
            itemView.setOnCreateContextMenuListener(this)

            itemView.setOnClickListener {
                listen.onElementSelected(filteredElementList.get(adapterPosition))
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu!!.add(0, 1, adapterPosition, "action 1")
            //установка бэкграунда контекстного меню
            val positionOfMenuItem = 0
            val item = menu.getItem(positionOfMenuItem)
            val s = SpannableString("Удалить")
            s.setSpan(ForegroundColorSpan(Color.BLACK), 0, s.length, 0)
            item.title = s
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element: Element = list.get(position)
            holder.titleV?.text = element.title
            holder.messV?.text = element.message
        holder.linLay?.setOnLongClickListener(OnLongClickListener {
            setPosition(holder.adapterPosition)
            false
        })
    }

    override fun getItemCount(): Int {
        return list.size
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

    fun addAll(listA: MutableList<Element>) {
        list.addAll(listA)
        notifyDataSetChanged()
    }


    fun getFilter(): Filter {
        return elementFilter
    }

    private val elementFilter: Filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charString: String = constraint.toString()
            if (charString.isEmpty()){
                filteredElementList = list
            } else {
                val filteredList: MutableList<Element> = mutableListOf()
                for (row: Element in list){
                    if (row.title.lowercase().contains(charString.lowercase()) || row.message.lowercase().contains(charString.lowercase())){
                        filteredList.add(row)
                    }
                }
                filteredElementList = filteredList
            }
            val filterResults: FilterResults = FilterResults()
            filterResults.values = filteredElementList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredElementList = results?.values as MutableList<Element>
            notifyDataSetChanged()
        }

    }
    inner class ElementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ll: LinearLayout
        var msg: TextView
        var ttl: TextView
        init {
            ttl = itemView.findViewById(R.id.title_item)
            msg = itemView.findViewById(R.id.mess_item)
            ll = itemView.findViewById(R.id.items_list)
            itemView.setOnClickListener {
                listen.onElementSelected(filteredElementList.get(adapterPosition))
            }
        }
    }


//    private val elementFilter: Filter = object : Filter() {
//        override fun performFiltering(constraint: CharSequence?): FilterResults {
//            val filteredList: MutableList<Element> = mutableListOf()
//            if (constraint == null || constraint.isEmpty()){
//                filteredList.addAll(listFull)
//            } else {
//                val filterPattern = constraint.toString().lowercase().trim()
//                for (item in listFull){
//                    if (item.title.lowercase().contains(filterPattern)){
//                        filteredList.add(item)
//                    }
//                }
//            }
//            val res = FilterResults()
//            res.values = filteredList
//            return res
//        }
//
//        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//            list.clear()
//            list.addAll(results?.values as MutableList<Element>)
//            notifyDataSetChanged()
//        }
//
//    }

    interface ElementAdapterListener {
        fun onElementSelected(elts: Element?)
    }
}