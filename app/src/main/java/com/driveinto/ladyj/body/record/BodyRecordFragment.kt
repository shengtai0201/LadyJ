package com.driveinto.ladyj.body.record

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.driveinto.ladyj.DetailOperations
import com.driveinto.ladyj.ListCallback

import com.driveinto.ladyj.R
import com.driveinto.ladyj.app.AbstractFragment
import com.driveinto.ladyj.body.Body
import kotlinx.android.synthetic.main.fragment_master.view.*

class BodyRecordFragment : AbstractFragment(), ListCallback<BodyRecord> {

    private val viewModel: BodyRecordViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return BodyRecordViewModel(activity!!.application) as T
            }
        }
    }

    private lateinit var body: Body

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = BodyRecordFragmentArgs.fromBundle(it)
            body = args.body
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_master, container, false)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        view.master.addItemDecoration(decoration)

        view.fab.setOnClickListener { onItemChanging(null, DetailOperations.Create) }

        // init adapter
        val adapter = BodyRecordRecyclerViewAdapter(this)
        view.master.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.message.observe(viewLifecycleOwner, Observer {
            showToast("\uD83D\uDE28 Wooops $it")
        })

        // setup scroll listener
        val layoutManager = view.master.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
        view.master.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(body, visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })

        // query
        viewModel.list(body)

        return view
    }

    override fun onItemChanging(entity: BodyRecord?, operation: DetailOperations) {
        if (resources.getBoolean(R.bool.twoPane)) {
            // 明細一同顯示
            val detail = BodyRecordDetailFragment.newInstance(body, entity, operation.value)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.detail_container, detail).commit()
        } else {
            // 導向明細
            val controller = Navigation.findNavController(requireActivity(), R.id.nav_master_controller)
            val action =
                BodyRecordFragmentDirections.actionNavBodyRecordToNavBodyRecordDetail(body, entity, operation.value)
            controller.navigate(action)
        }

        if (entity != null) {
            Log.i(
                LOG_TAG,
                "已選取 BodyRecord, DateMillis: ${entity.dateMillis}, BodyId: ${entity.bodyId}, Dirty: ${entity.dirty}"
            )
        }
    }

    companion object {
        private const val LOG_TAG = "BodyRecordFragment"
    }
}
