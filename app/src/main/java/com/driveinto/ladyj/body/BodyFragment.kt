package com.driveinto.ladyj.body

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

import com.driveinto.ladyj.R
import com.driveinto.ladyj.app.AbstractFragment
import kotlinx.android.synthetic.main.fragment_body.view.*
import kotlinx.android.synthetic.main.fragment_body.view.detail_cancel
import kotlinx.android.synthetic.main.fragment_body.view.detail_ok
import kotlinx.android.synthetic.main.fragment_customer_detail.view.*

class BodyFragment : AbstractFragment() {

    private val viewModel: BodyViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return BodyViewModel(activity!!.application) as T
            }
        }
    }

    private lateinit var body: Body
    private lateinit var authorization: DetailAuthorizations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = BodyFragmentArgs.fromBundle(it)
            body = args.body
            authorization = DetailAuthorizations.fromValue(args.authorizationValue)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_body, container, false)

        view.detail_body_health_spine.isChecked = body.healthSpine
        view.detail_body_health_backache.isChecked = body.healthBackache
        setText(view.detail_body_health_other, body.healthOther)
        view.detail_body_curve_breast_enhancement.isChecked = body.curveBreastEnhancement
        view.detail_body_curve_breast_reduction.isChecked = body.curveBreastReduction
        view.detail_body_curve_breast_care.isChecked = body.curveBreastCare
        view.detail_body_curve_arm.isChecked = body.curveArm
        view.detail_body_curve_hip.isChecked = body.curveHip
        view.detail_body_curve_stomach.isChecked = body.curveStomach
        view.detail_body_curve_waist.isChecked = body.curveWaist
        view.detail_body_curve_abdominal.isChecked = body.curveAbdominal
        view.detail_body_curve_thigh.isChecked = body.curveThigh
        view.detail_body_curve_calf.isChecked = body.curveCalf
        view.detail_body_curve_fat_soft.isChecked = body.curveFatSoft
        view.detail_body_curve_fat_hard.isChecked = body.curveFatHard
        view.detail_body_curve_fat_cellulite.isChecked = body.curveFatCellulite
        view.detail_body_curve_fat_tangled.isChecked = body.curveFatTangled
        setText(view.detail_body_curve_fat_other, body.curveFatOther)

        // UI 控制
        if (body.dirty != null) {
            view.body.visibility = View.VISIBLE
        }
        if (authorization == DetailAuthorizations.ReadOnly) {
            view.detail_body_health_spine.isEnabled = false
            view.detail_body_health_backache.isEnabled = false
            view.detail_body_health_other.isEnabled = false
            view.detail_body_curve_breast_enhancement.isEnabled = false
            view.detail_body_curve_breast_reduction.isEnabled = false
            view.detail_body_curve_breast_care.isEnabled = false
            view.detail_body_curve_arm.isEnabled = false
            view.detail_body_curve_hip.isEnabled = false
            view.detail_body_curve_stomach.isEnabled = false
            view.detail_body_curve_waist.isEnabled = false
            view.detail_body_curve_abdominal.isEnabled = false
            view.detail_body_curve_thigh.isEnabled = false
            view.detail_body_curve_calf.isEnabled = false
            view.detail_body_curve_fat_soft.isEnabled = false
            view.detail_body_curve_fat_hard.isEnabled = false
            view.detail_body_curve_fat_cellulite.isEnabled = false
            view.detail_body_curve_fat_tangled.isEnabled = false
            view.detail_body_curve_fat_other.isEnabled = false

            view.detail_ok.visibility = View.GONE
        }

        view.detail_body_health_spine.setOnCheckedChangeListener { _, isChecked -> body.healthSpine = isChecked }
        view.detail_body_health_backache.setOnCheckedChangeListener { _, isChecked ->
            body.healthBackache = isChecked
        }
        view.detail_body_curve_breast_enhancement.setOnCheckedChangeListener { _, isChecked ->
            body.curveBreastEnhancement = isChecked
        }
        view.detail_body_curve_breast_reduction.setOnCheckedChangeListener { _, isChecked ->
            body.curveBreastReduction = isChecked
        }
        view.detail_body_curve_breast_care.setOnCheckedChangeListener { _, isChecked ->
            body.curveBreastCare = isChecked
        }
        view.detail_body_curve_arm.setOnCheckedChangeListener { _, isChecked -> body.curveArm = isChecked }
        view.detail_body_curve_hip.setOnCheckedChangeListener { _, isChecked -> body.curveHip = isChecked }
        view.detail_body_curve_stomach.setOnCheckedChangeListener { _, isChecked -> body.curveStomach = isChecked }
        view.detail_body_curve_waist.setOnCheckedChangeListener { _, isChecked -> body.curveWaist = isChecked }
        view.detail_body_curve_abdominal.setOnCheckedChangeListener { _, isChecked ->
            body.curveAbdominal = isChecked
        }
        view.detail_body_curve_thigh.setOnCheckedChangeListener { _, isChecked -> body.curveThigh = isChecked }
        view.detail_body_curve_calf.setOnCheckedChangeListener { _, isChecked -> body.curveCalf = isChecked }
        view.detail_body_curve_fat_soft.setOnCheckedChangeListener { _, isChecked -> body.curveFatSoft = isChecked }
        view.detail_body_curve_fat_hard.setOnCheckedChangeListener { _, isChecked -> body.curveFatHard = isChecked }
        view.detail_body_curve_fat_cellulite.setOnCheckedChangeListener { _, isChecked ->
            body.curveFatCellulite = isChecked
        }
        view.detail_body_curve_fat_tangled.setOnCheckedChangeListener { _, isChecked ->
            body.curveFatTangled = isChecked
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.detail_body_data.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(), R.id.nav_master_controller)
            val action = BodyFragmentDirections.actionNavBodyToNavBodyData(body, authorization.value)
            controller.navigate(action)
        }
        view.detail_body_record.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(), R.id.nav_master_controller)
            val action = BodyFragmentDirections.actionNavBodyToNavBodyRecord(body, authorization.value)
            controller.navigate(action)
        }
        view.detail_body_record_data.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(), R.id.nav_master_controller)
            val action = BodyFragmentDirections.actionNavBodyToNavBodyRecordData(body, authorization.value)
            controller.navigate(action)
        }

        view.detail_ok.setOnClickListener {
            setString(view.detail_body_health_other) { body.healthOther = it }
            setString(view.detail_body_curve_fat_other) { body.curveFatOther = it }
            body.dirty = true

            viewModel.modify(body)

            reply()
        }
        view.detail_cancel.setOnClickListener {
            reply()
        }
    }

    private fun reply() {
        val controller = Navigation.findNavController(requireActivity(), R.id.nav_master_controller)
        controller.navigateUp()
    }
}
