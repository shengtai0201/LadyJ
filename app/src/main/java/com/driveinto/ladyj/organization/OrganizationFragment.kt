package com.driveinto.ladyj.organization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.driveinto.ladyj.DetailAuthorizations

import com.driveinto.ladyj.R
import com.driveinto.ladyj.app.AbstractFragment
import com.driveinto.ladyj.login.LoginFragmentDirections
import com.driveinto.ladyj.login.LoginResult
import de.blox.graphview.Graph
import de.blox.graphview.Node
import de.blox.graphview.tree.BuchheimWalkerAlgorithm
import de.blox.graphview.tree.BuchheimWalkerConfiguration
import kotlinx.android.synthetic.main.fragment_organization.view.*
import kotlinx.android.synthetic.main.fragment_organization.view.graph

class OrganizationFragment : AbstractFragment() {

    private val viewModel: OrganizationViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return OrganizationViewModel(activity!!.application) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_organization, container, false)

        viewModel.data.observe(viewLifecycleOwner, Observer { organizations ->
            // create graph
            val graph = Graph()
            val nodes: MutableMap<String, Node> = mutableMapOf()
            organizations.forEach {
                val sourceNode: Node = if (nodes.containsKey(it.source.id)) {
                    nodes[it.source.id]!!
                } else {
                    Node(it.source)
                }

                val destinationNode: Node = if (nodes.containsKey(it.destination.id)) {
                    nodes[it.destination.id]!!
                } else {
                    Node(it.destination)
                }

                graph.addEdge(sourceNode, destinationNode)
            }

            // setup adapter
            val adapter = OrganizationAdapter(graph)
            val configuration = BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(300)
                .setSubtreeSeparation(300)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build()
            adapter.algorithm = BuchheimWalkerAlgorithm(configuration)
            view.graph.adapter = adapter
            view.graph.setOnItemClickListener { _, _, position, _ ->
                val node = adapter.getNode(position)
                val user = node.data as Organization.User
                showSnackBar(view.graph, "Clicked on ${user.userName}")

                val controller = Navigation.findNavController(activity!!, R.id.nav_master_controller)
                val action = OrganizationFragmentDirections.actionNavOrganizationToNavCustomer(
                    DetailAuthorizations.ReadOnly.value,
                    user.id
                )
                controller.navigate(action)
            }
        })
        viewModel.message.observe(viewLifecycleOwner, Observer {
            showToast("\uD83D\uDE28 Wooops $it")
        })

        // query
        viewModel.list(getLoginResult()!!)

        return view
    }

    companion object {
        private const val LOG_TAG = "OrganizationFragment"
    }
}
