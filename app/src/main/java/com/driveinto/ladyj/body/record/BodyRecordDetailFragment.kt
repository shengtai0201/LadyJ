package com.driveinto.ladyj.body.record

import android.os.Bundle
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
import com.driveinto.ladyj.app.AbstractFragment
import com.driveinto.ladyj.body.Body
import kotlinx.android.synthetic.main.fragment_body_record_detail.view.*

class BodyRecordDetailFragment : AbstractFragment() {

    companion object {
        fun newInstance(body: Body, bodyRecord: BodyRecord?, operationValue: Int) =
            BodyRecordDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Body.key, body)
                    putParcelable(BodyRecord.key, bodyRecord)
                    putInt(DetailOperations.key, operationValue)
                }
            }
    }

    private val viewModel: BodyRecordViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return BodyRecordViewModel(activity!!.application) as T
            }
        }
    }

    private lateinit var body: Body
    private lateinit var bodyRecord: BodyRecord
    private lateinit var detailOperation: DetailOperations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            body = if (it.containsKey(Body.key)) {
                it.getParcelable(Body.key)!!
            } else {
                val args = BodyRecordDetailFragmentArgs.fromBundle(it)
                args.body
            }

            val bodyRecord = if (it.containsKey(BodyRecord.key)) {
                it.getParcelable(BodyRecord.key)
            } else {
                val args = BodyRecordDetailFragmentArgs.fromBundle(it)
                args.bodyRecord
            }
            if (bodyRecord == null) {
                this.bodyRecord = BodyRecord.emptyInstance(body.customerId)
            } else {
                this.bodyRecord = bodyRecord
            }

            val operationValue = if (it.containsKey(DetailOperations.key)) {
                it.getInt(DetailOperations.key)
            } else {
                val args = BodyRecordDetailFragmentArgs.fromBundle(it)
                args.operationValue
            }
            detailOperation = DetailOperations.fromValue(operationValue)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_body_record_detail, container, false)

        setText(view.body_record_upper_bust, bodyRecord.upperBust)
        setText(view.body_record_under_bust, bodyRecord.underBust)
        setText(view.body_record_cup_size, bodyRecord.cupSize)
        setText(view.body_record_left_arm, bodyRecord.leftArm)
        setText(view.body_record_right_arm, bodyRecord.rightArm)
        setText(view.body_record_stomach, bodyRecord.stomach)
        setText(view.body_record_waist, bodyRecord.waist)
        setText(view.body_record_abdominal, bodyRecord.abdominal)
        setText(view.body_record_hip, bodyRecord.hip)
        setText(view.body_record_left_leg, bodyRecord.leftLeg)
        setText(view.body_record_right_leg, bodyRecord.rightLeg)

        when (detailOperation) {
            DetailOperations.Create -> view.detail_ok.text = getString(R.string.detail_create)
            DetailOperations.Update -> view.detail_ok.text = getString(R.string.detail_update)
            DetailOperations.Destroy -> {
                view.body_record_upper_bust.isEnabled = false
                view.body_record_under_bust.isEnabled = false
                view.body_record_cup_size.isEnabled = false
                view.body_record_left_arm.isEnabled = false
                view.body_record_right_arm.isEnabled = false
                view.body_record_stomach.isEnabled = false
                view.body_record_waist.isEnabled = false
                view.body_record_abdominal.isEnabled = false
                view.body_record_hip.isEnabled = false
                view.body_record_left_leg.isEnabled = false
                view.body_record_right_leg.isEnabled = false

                view.detail_ok.text = getString(R.string.detail_destroy)
            }
            else -> view.detail_ok.text = getString(R.string.detail_ok)
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.detail_ok.setOnClickListener {
            setInt(view.body_record_upper_bust) { bodyRecord.upperBust = it }
            setInt(view.body_record_under_bust) { bodyRecord.underBust = it }
            setInt(view.body_record_cup_size) { bodyRecord.cupSize = it }
            setInt(view.body_record_left_arm) { bodyRecord.leftArm = it }
            setInt(view.body_record_right_arm) { bodyRecord.rightArm = it }
            setInt(view.body_record_stomach) { bodyRecord.stomach = it }
            setInt(view.body_record_waist) { bodyRecord.waist = it }
            setInt(view.body_record_abdominal) { bodyRecord.abdominal = it }
            setInt(view.body_record_hip) { bodyRecord.hip = it }
            setInt(view.body_record_left_leg) { bodyRecord.leftLeg = it }
            setInt(view.body_record_right_leg) { bodyRecord.rightLeg = it }
            bodyRecord.dirty = true

            when (detailOperation) {
                DetailOperations.Create -> viewModel.insert(body, bodyRecord)
                DetailOperations.Update -> viewModel.update(bodyRecord)
                DetailOperations.Destroy -> viewModel.delete(bodyRecord)
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
