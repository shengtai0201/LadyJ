package com.driveinto.ladyj.organization

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.driveinto.ladyj.R
import de.blox.graphview.BaseGraphAdapter
import de.blox.graphview.Graph

class OrganizationAdapter(graph: Graph) : BaseGraphAdapter<OrganizationAdapter.ViewHolder>(graph) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.node_organization, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, data: Any?, position: Int) {
        viewHolder!!.bind(data!! as Organization.User)
    }

    inner class ViewHolder(view: View) : de.blox.graphview.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.textView)

        fun bind(user: Organization.User) {
            textView.text = user.userName
        }
    }
}