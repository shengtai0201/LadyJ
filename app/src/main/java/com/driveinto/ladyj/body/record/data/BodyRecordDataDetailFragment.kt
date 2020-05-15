package com.driveinto.ladyj.body.record.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.driveinto.ladyj.DetailAuthorizations
import com.driveinto.ladyj.DetailOperations

import com.driveinto.ladyj.R
import com.driveinto.ladyj.app.AbstractFragment
import com.driveinto.ladyj.body.Body
import com.driveinto.ladyj.customer.CustomerDetailFragmentArgs
import kotlinx.android.synthetic.main.fragment_body_record_data_detail.view.*
import kotlinx.android.synthetic.main.fragment_body_record_data_detail.view.detail_cancel
import kotlinx.android.synthetic.main.fragment_body_record_data_detail.view.detail_ok
import kotlinx.android.synthetic.main.fragment_customer_detail.view.*

class BodyRecordDataDetailFragment : AbstractFragment() {

    companion object {
        fun newInstance(body: Body, bodyRecordData: BodyRecordData?, operationValue: Int, authorizationValue: Int) =
            BodyRecordDataDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Body.key, body)
                    putParcelable(BodyRecordData.key, bodyRecordData)
                    putInt(DetailOperations.key, operationValue)
                    putInt(DetailAuthorizations.key, authorizationValue)
                }
            }
    }

    private val viewModel: BodyRecordDataViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return BodyRecordDataViewModel(activity!!.application) as T
            }
        }
    }

    private lateinit var body: Body
    private lateinit var bodyRecordData: BodyRecordData
    private lateinit var operation: DetailOperations
    private lateinit var authorization: DetailAuthorizations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            body = if (it.containsKey(Body.key)) {
                it.getParcelable(Body.key)!!
            } else {
                val args = BodyRecordDataDetailFragmentArgs.fromBundle(it)
                args.body
            }

            val bodyRecordData = if (it.containsKey(BodyRecordData.key)) {
                it.getParcelable(BodyRecordData.key)
            } else {
                val args = BodyRecordDataDetailFragmentArgs.fromBundle(it)
                args.bodyRecordData
            }
            if (bodyRecordData == null) {
                this.bodyRecordData = BodyRecordData.emptyInstance(body.customerId)
            } else {
                this.bodyRecordData = bodyRecordData
            }

            val operationValue = if (it.containsKey(DetailOperations.key)) {
                it.getInt(DetailOperations.key)
            } else {
                val args = BodyRecordDataDetailFragmentArgs.fromBundle(it)
                args.operationValue
            }
            operation = DetailOperations.fromValue(operationValue)!!

            val authorizationValue = if (it.containsKey(DetailAuthorizations.key)) {
                it.getInt(DetailAuthorizations.key)
            } else {
                val args = CustomerDetailFragmentArgs.fromBundle(it)
                args.authorizationValue
            }
            authorization = DetailAuthorizations.fromValue(authorizationValue)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_body_record_data_detail, container, false)

        setText(view.body_record_data_design, bodyRecordData.design)
        setText(view.body_record_data_buy, bodyRecordData.buy)

        // UI 控制
        if (authorization == DetailAuthorizations.ReadOnly) {
            view.body_record_data_design.isEnabled = false
            view.body_record_data_buy.isEnabled = false

            view.detail_ok.visibility = View.GONE
        } else {
            when (operation) {
                DetailOperations.Create -> view.detail_ok.text = getString(R.string.detail_create)
                DetailOperations.Update -> view.detail_ok.text = getString(R.string.detail_update)
                DetailOperations.Destroy -> {
                    view.body_record_data_design.isEnabled = false
                    view.body_record_data_buy.isEnabled = false

                    view.detail_ok.text = getString(R.string.detail_destroy)
                }
                else -> view.detail_ok.text = getString(R.string.detail_ok)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.detail_ok.setOnClickListener {
            setString(view.body_record_data_design) { bodyRecordData.design = it }
            setString(view.body_record_data_buy) { bodyRecordData.buy = it }
            bodyRecordData.dirty = true

            when (operation) {
                DetailOperations.Create -> viewModel.insert(body, bodyRecordData)
                DetailOperations.Update -> viewModel.update(bodyRecordData)
                DetailOperations.Destroy -> viewModel.delete(bodyRecordData)
                else -> return@setOnClickListener
            }

            reply()
        }
        view.detail_cancel.setOnClickListener {
            reply()
        }
    }

    private fun reply() {
        if (resources.getBoolean(R.bool.twoPane)) {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        } else {
            val controller = Navigation.findNavController(requireActivity(), R.id.nav_master_controller)
            controller.navigateUp()
        }
    }
}
