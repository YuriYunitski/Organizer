package com.yunitski.organizer

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class ElementAdapter(private val list: MutableList<Element>): RecyclerView.Adapter<ElementAdapter.MyViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var titleV: TextView? = null
        var messV: TextView? = null


        init {
            titleV = itemView.findViewById(R.id.title_item)
            messV = itemView.findViewById(R.id.mess_item)
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)

            }
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
    private var position = 0

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int) {
        this.position = position
    }
    fun getItem(position: Int) = list.get(position)
}