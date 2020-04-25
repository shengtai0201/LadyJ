package com.driveinto.ladyj.skin.record

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

class SkinRecordRecyclerViewAdapter(private val callback: ListCallback<SkinRecord>) :
    ListAdapter<SkinRecord, SkinRecordRecyclerViewAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.master_skin_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skinRecord = getItem(position)
        holder.bind(skinRecord)

        with(holder.itemView) {
            tag = skinRecord
            setOnClickListener { callback.onItemChanging(skinRecord, DetailOperations.Update) }
            setOnLongClickListener {
                callback.onItemChanging(skinRecord, DetailOperations.Destroy)
                true
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date: TextView = view.date

        fun bind(skinRecord: SkinRecord) {
            date.text = Converters.toDateString(skinRecord.dateMillis)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SkinRecord>() {
            override fun areItemsTheSame(oldItem: SkinRecord, newItem: SkinRecord): Boolean =
                oldItem.dateMillis == newItem.dateMillis && oldItem.skinId == newItem.skinId

            override fun areContentsTheSame(oldItem: SkinRecord, newItem: SkinRecord): Boolean = oldItem == newItem
        }
    }
}