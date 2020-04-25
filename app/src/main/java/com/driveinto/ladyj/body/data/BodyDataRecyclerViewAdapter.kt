package com.driveinto.ladyj.body.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.driveinto.ladyj.DetailOperations
import com.driveinto.ladyj.ListCallback
import com.driveinto.ladyj.R
import com.driveinto.ladyj.room.Converters
import kotlinx.android.synthetic.main.master_body_data.view.*

class BodyDataRecyclerViewAdapter(private val callback: ListCallback<BodyData>) :
    ListAdapter<BodyData, BodyDataRecyclerViewAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.master_body_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bodyData = getItem(position)
        holder.bind(bodyData)

        with(holder.itemView) {
            tag = bodyData
            setOnClickListener { callback.onItemChanging(bodyData, DetailOperations.Update) }
            setOnLongClickListener {
                callback.onItemChanging(bodyData, DetailOperations.Destroy)
                true
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date: TextView = view.date

        fun bind(bodyData: BodyData) {
            date.text = Converters.toDateString(bodyData.dateMillis)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<BodyData>() {
            override fun areItemsTheSame(oldItem: BodyData, newItem: BodyData): Boolean =
                oldItem.dateMillis == newItem.dateMillis && oldItem.bodyId == newItem.bodyId

            override fun areContentsTheSame(oldItem: BodyData, newItem: BodyData): Boolean = oldItem == newItem
        }
    }
}