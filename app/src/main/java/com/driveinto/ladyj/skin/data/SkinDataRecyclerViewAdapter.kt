package com.driveinto.ladyj.skin.data

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

class SkinDataRecyclerViewAdapter(private val callback: ListCallback<SkinData>) :
    ListAdapter<SkinData, SkinDataRecyclerViewAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.master_skin_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skinData = getItem(position)
        holder.bind(skinData)

        with(holder.itemView) {
            tag = skinData
            setOnClickListener { callback.onItemChanging(skinData, DetailOperations.Update) }
            setOnLongClickListener {
                callback.onItemChanging(skinData, DetailOperations.Destroy)
                true
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date: TextView = view.date

        fun bind(skinData: SkinData) {
            date.text = Converters.toDateString(skinData.dateMillis)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SkinData>() {
            override fun areItemsTheSame(oldItem: SkinData, newItem: SkinData): Boolean =
                oldItem.dateMillis == newItem.dateMillis && oldItem.skinId == newItem.skinId

            override fun areContentsTheSame(oldItem: SkinData, newItem: SkinData): Boolean = oldItem == newItem
        }
    }
}