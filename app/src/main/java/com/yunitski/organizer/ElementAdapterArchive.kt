package com.yunitski.organizer

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ElementAdapterArchive(private val context: Context, private val list: MutableList<Element>, private val listen: ElementAdapterListener): RecyclerView.Adapter<ElementAdapterArchive.MyViewHolder>(),
    Filterable {

    var filteredElementList: MutableList<Element> = mutableListOf()
    init {
        filteredElementList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementAdapterArchive.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElementAdapterArchive.MyViewHolder, position: Int) {
        val element: Element = filteredElementList[position]
        holder.titleV.text = element.title
        holder.messV.text = element.message
        holder.idItem.text = element.id
        holder.dateV.text = element.date
        holder.timeV.text = element.time
        holder.linL.setOnLongClickListener {
            setPosition(element.id.toInt())
            false
        }
    }



    override fun getItemCount() = filteredElementList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        val titleV: TextView = itemView.findViewById(R.id.title_item)
        val messV: TextView = itemView.findViewById(R.id.mess_item)
        val idItem: TextView = itemView.findViewById(R.id.id_item)
        val linL: LinearLayout = itemView.findViewById(R.id.items_list)
        val dateV: TextView = itemView.findViewById(R.id.date_item)
        val timeV: TextView = itemView.findViewById(R.id.time_item)

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
            val s = SpannableString("Восстановить")
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

    override fun onViewRecycled(holder: MyViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }
}