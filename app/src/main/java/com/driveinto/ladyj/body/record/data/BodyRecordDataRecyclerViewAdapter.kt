package com.driveinto.ladyj.body.record.data

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

class BodyRecordDataRecyclerViewAdapter(private val callback: ListCallback<BodyRecordData>) :
    ListAdapter<BodyRecordData, BodyRecordDataRecyclerViewAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.master_body_record_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bodyRecordData = getItem(position)
        holder.bind(bodyRecordData)

        with(holder.itemView) {
            tag = bodyRecordData
            setOnClickListener { callback.onItemChanging(bodyRecordData, DetailOperations.Update) }
            setOnLongClickListener {
                callback.onItemChanging(bodyRecordData, DetailOperations.Destroy)
                true
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date: TextView = view.date

        fun bind(bodyRecordData: BodyRecordData) {
            date.text = Converters.toDateString(bodyRecordData.dateMillis)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<BodyRecordData>() {
            override fun areItemsTheSame(oldItem: BodyRecordData, newItem: BodyRecordData): Boolean =
                oldItem.dateMillis == newItem.dateMillis && oldItem.bodyId == newItem.bodyId

            override fun areContentsTheSame(oldItem: BodyRecordData, newItem: BodyRecordData): Boolean =
                oldItem == newItem
        }
    }
}