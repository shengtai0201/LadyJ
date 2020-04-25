package com.driveinto.ladyj.body.record

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

class BodyRecordRecyclerViewAdapter(private val callback: ListCallback<BodyRecord>) :
    ListAdapter<BodyRecord, BodyRecordRecyclerViewAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.master_body_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bodyRecord = getItem(position)
        holder.bind(bodyRecord)

        with(holder.itemView) {
            tag = bodyRecord
            setOnClickListener { callback.onItemChanging(bodyRecord, DetailOperations.Update) }
            setOnLongClickListener {
                callback.onItemChanging(bodyRecord, DetailOperations.Destroy)
                true
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date: TextView = view.date

        fun bind(bodyRecord: BodyRecord) {
            date.text = Converters.toDateString(bodyRecord.dateMillis)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<BodyRecord>() {
            override fun areItemsTheSame(oldItem: BodyRecord, newItem: BodyRecord): Boolean =
                oldItem.dateMillis == newItem.dateMillis && oldItem.bodyId == newItem.bodyId

            override fun areContentsTheSame(oldItem: BodyRecord, newItem: BodyRecord): Boolean = oldItem == newItem
        }
    }
}