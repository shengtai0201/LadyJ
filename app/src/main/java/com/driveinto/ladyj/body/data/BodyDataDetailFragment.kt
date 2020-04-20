package com.driveinto.ladyj.body.data

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.driveinto.ladyj.DetailOperations

import com.driveinto.ladyj.R
import com.driveinto.ladyj.body.Body
import com.driveinto.ladyj.customer.CustomerDetailFragmentArgs
import kotlinx.android.synthetic.main.fragment_body_data_detail.view.*

class BodyDataDetailFragment : Fragment() {

    companion object {
        fun newInstance(body: Body, bodyData: BodyData?, operationValue: Int) =
            BodyDataDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Body.key, body)
                    putParcelable(BodyData.key, bodyData)
                    putInt(DetailOperations.key, operationValue)
                }
            }
    }

    private val viewModel: BodyDataViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return BodyDataViewModel(activity!!.application) as T
            }
        }
    }

    private lateinit var body: Body
    private lateinit var bodyData: BodyData
    private lateinit var detailOperation: DetailOperations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            body = if (it.containsKey(Body.key)) {
                it.getParcelable(Body.key)!!
            } else {
                val args = BodyDataDetailFragmentArgs.fromBundle(it)
                args.body
            }

            val bodyData = if (it.containsKey(BodyData.key)) {
                it.getParcelable(BodyData.key)
            } else {
                val args = BodyDataDetailFragmentArgs.fromBundle(it)
                args.bodyData
            }
            if (bodyData == null) {
                this.bodyData = BodyData.emptyInstance(body.customerId)
            } else {
                this.bodyData = bodyData
            }

            val operationValue = if (it.containsKey(DetailOperations.key)) {
                it.getInt(DetailOperations.key)
            } else {
                val args = CustomerDetailFragmentArgs.fromBundle(it)
                args.operationValue
            }
            detailOperation = DetailOperations.fromValue(operationValue)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_body_data_detail, container, false)

        if(bodyData.remark != null){
            view.body_data_remark.setText(bodyData.remark)
        }
        if(bodyData.diagnosis != null){
            view.body_data_diagnosis.setText(bodyData.diagnosis)
        }

        when (detailOperation) {
            DetailOperations.Create -> view.detail_ok.text = getString(R.string.detail_create)
            DetailOperations.Update ->  view.detail_ok.text = getString(R.string.detail_update)
            DetailOperations.Destroy -> {
                view.body_data_remark.isEnabled = false
                view.body_data_diagnosis.isEnabled = false

                view.detail_ok.text = getString(R.string.detail_destroy)
            }
            else -> view.detail_ok.text = getString(R.string.detail_ok)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.detail_ok.setOnClickListener {
            bodyData.remark = view.body_data_remark.text.toString()
            bodyData.diagnosis = view.body_data_diagnosis.text.toString()
            bodyData.dirty = true

            when(detailOperation){
                DetailOperations.Create -> viewModel.insert(body, bodyData)
                DetailOperations.Update -> viewModel.update(bodyData)
                DetailOperations.Destroy -> viewModel.delete(bodyData)
                else -> return@setOnClickListener
            }

            reply()
        }
        view.detail_cancel.setOnClickListener {
            reply()
        }
    }

    private fun reply() {
        if(resources.getBoolean(R.bool.twoPane)){
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }else{
            val controller = Navigation.findNavController(requireActivity(), R.id.nav_master_controller)
            controller.navigateUp()
        }
    }
}
