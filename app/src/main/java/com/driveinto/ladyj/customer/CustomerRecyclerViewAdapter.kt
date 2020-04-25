package com.driveinto.ladyj.customer

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
import kotlinx.android.synthetic.main.master_customer.view.*

class CustomerRecyclerViewAdapter(private val callback: ListCallback<Customer>) :
    ListAdapter<Customer, CustomerRecyclerViewAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.master_customer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customer = getItem(position)
        holder.bind(customer)

        with(holder.itemView) {
            tag = customer
            setOnClickListener { callback.onItemChanging(customer, DetailOperations.Update) }
            setOnLongClickListener {
                callback.onItemChanging(customer, DetailOperations.Destroy)
                true
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameView: TextView = view.name
        private val phoneView: TextView = view.phone

        fun bind(customer: Customer) {
            nameView.text = customer.name
            phoneView.text = customer.phone
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Customer>() {
            override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean = oldItem == newItem
        }
    }
}