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
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class ElementAdapter(private val context: Context, private val list: MutableList<Element>, private val listen: ElementAdapterListener): RecyclerView.Adapter<ElementAdapter.MyViewHolder>(), Filterable {

    var filteredElementList: MutableList<Element> = mutableListOf()
    init {
        filteredElementList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementAdapter.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElementAdapter.MyViewHolder, position: Int) {
        val element: Element = filteredElementList[position]
        holder.titleV.text = element.title
        holder.messV.text = element.message
        holder.idItem.text = element.id
        holder.linL.setOnLongClickListener(OnLongClickListener {
            setPosition(element.id.toInt())
            false
        })
    }



    override fun getItemCount() = filteredElementList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        val titleV: TextView = itemView.findViewById(R.id.title_item)
        val messV: TextView = itemView.findViewById(R.id.mess_item)
        val idItem: TextView = itemView.findViewById(R.id.id_item)
        val linL: LinearLayout = itemView.findViewById(R.id.items_list)

        init {
            itemView.setOnClickListener{
                listen.onElementSelected(filteredElementList[adapterPosition])
            }
            itemView.setOnCreateContextMenuListener(this)
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

    fun setPosition(position: Int) {
        this.position = position
    }

    private val elementFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
//            val charString: String = constraint.toString()
//            filteredElementList = if (charString.isEmpty()){
//                list
//            } else {
//                val filteredList: MutableList<Element> = mutableListOf()
//                for (row: Element in list){
//                    if (row.title.lowercase().contains(charString.lowercase()) || row.message.lowercase().contains(charString.lowercase())){
//                        filteredList.add(row)
//                    }
//                }
//                filteredList
//            }
//            val filterResults: FilterResults = FilterResults()
//            filterResults.values = filteredElementList
//            return filterResults
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
//            notifyDataSetChanged()

//            filteredElementList = results?.values as MutableList<Element>
//            list.addAll(results?.values as MutableList<Element>)
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

    fun addAll(listA: MutableList<Element>) {
        list.addAll(listA)
        filteredElementList.addAll(listA)
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: MyViewHolder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder)
    }

    //    var onItemClick: ((Int) -> Unit)? = null
//    private var position = 0
//
//    private fun setPosition(position: Int) {
//        this.position = position
//    }
//    fun getPosition(): Int {
//        return position
//    }
//
//    var filteredElementList: MutableList<Element> = mutableListOf()
//    init {
//        filteredElementList = list
////        for (i in list){
////            filteredElementList.addAll(listOf(i))
//        }
//
//    }
//
//
//    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
//        View.OnCreateContextMenuListener {
//        var titleV: TextView? = null
//        var messV: TextView? = null
//        var linLay: LinearLayout? = null
//
//        init {
//            titleV = itemView.findViewById(R.id.title_item)
//            messV = itemView.findViewById(R.id.mess_item)
//            linLay = itemView.findViewById(R.id.items_list)
//            itemView.setOnCreateContextMenuListener(this)
//
//            itemView.setOnClickListener {
//                listen.onElementSelected(filteredElementList[adapterPosition])
//            }
//        }
//
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
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
//        return MyViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val element: Element = list.get(position)
//            holder.titleV?.text = element.title
//            holder.messV?.text = element.message
//        holder.linLay?.setOnLongClickListener(OnLongClickListener {
//            setPosition(holder.adapterPosition)
//            false
//        })
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//    fun clear() {
//        val size: Int = list.size
//        if (size > 0) {
//            for (i in 0 until size) {
//                list.removeAt(0)
//            }
//            notifyItemRangeRemoved(0, size)
//        }
//    }
//
//    fun addAll(listA: MutableList<Element>) {
//        list.addAll(listA)
//        notifyDataSetChanged()
//    }
//
//
//    fun getFilter(): Filter {
//        return elementFilter
//    }
//
//    private val elementFilter: Filter = object : Filter(){
//        override fun performFiltering(constraint: CharSequence?): FilterResults {
//            val charString: String = constraint.toString()
//            filteredElementList = if (charString.isEmpty()){
//                list
//            } else {
//                val filteredList: MutableList<Element> = mutableListOf()
//                for (row: Element in list){
//                    if (row.title.lowercase().contains(charString.lowercase()) || row.message.lowercase().contains(charString.lowercase())){
//                        filteredList.add(row)
//                    }
//                }
//                filteredList
//            }
//            val filterResults: FilterResults = FilterResults()
//            filterResults.values = filteredElementList
//            return filterResults
//        }
//
//        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//            list.clear()
//            list.addAll(results?.values as MutableList<Element>)
////            filteredElementList = results?.values as MutableList<Element>
//            notifyDataSetChanged()
//        }
//
//    }
//
//
////    private val elementFilter: Filter = object : Filter() {
////        override fun performFiltering(constraint: CharSequence?): FilterResults {
////            val filteredList: MutableList<Element> = mutableListOf()
////            if (constraint == null || constraint.isEmpty()){
////                filteredList.addAll(listFull)
////            } else {
////                val filterPattern = constraint.toString().lowercase().trim()
////                for (item in listFull){
////                    if (item.title.lowercase().contains(filterPattern)){
////                        filteredList.add(item)
////                    }
////                }
////            }
////            val res = FilterResults()
////            res.values = filteredList
////            return res
////        }
////
////        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
////            list.clear()
////            list.addAll(results?.values as MutableList<Element>)
////            notifyDataSetChanged()
////        }
////
////    }
//
//    interface ElementAdapterListener {
//        fun onElementSelected(elts: Element?)
//    }
}