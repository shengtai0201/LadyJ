package com.driveinto.ladyj.skin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation

import com.driveinto.ladyj.R
import com.driveinto.ladyj.app.AbstractFragment
import kotlinx.android.synthetic.main.fragment_skin.view.*

class SkinFragment : AbstractFragment() {

    private val viewModel: SkinViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return SkinViewModel(activity!!.application) as T
            }
        }
    }

    private lateinit var skin: Skin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = SkinFragmentArgs.fromBundle(it)
            skin = args.skin
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_skin, container, false)

        view.detail_skin_condition_dry.isChecked = skin.conditionDry
        view.detail_skin_condition_oily.isChecked = skin.conditionOily
        view.detail_skin_condition_sensitivity.isChecked = skin.conditionSensitivity
        view.detail_skin_condition_mixed.isChecked = skin.conditionMixed
        view.detail_skin_improve_acne.isChecked = skin.improveAcne
        view.detail_skin_improve_sensitivity.isChecked = skin.improveSensitivity
        view.detail_skin_improve_wrinkles.isChecked = skin.improveWrinkles
        view.detail_skin_improve_pores.isChecked = skin.improvePores
        view.detail_skin_improve_spots.isChecked = skin.improveSpots
        view.detail_skin_improve_dull.isChecked = skin.improveDull
        view.detail_skin_improve_pock.isChecked = skin.improvePock
        setText(view.detail_skin_improve_other, skin.improveOther)

        if (skin.dirty != null) {
            view.skin.visibility = View.VISIBLE
        }

        view.detail_skin_condition_dry.setOnCheckedChangeListener { _, isChecked -> skin.conditionDry = isChecked }
        view.detail_skin_condition_oily.setOnCheckedChangeListener { _, isChecked ->
            skin.conditionOily = isChecked
        }
        view.detail_skin_condition_sensitivity.setOnCheckedChangeListener { _, isChecked ->
            skin.conditionSensitivity = isChecked
        }
        view.detail_skin_condition_mixed.setOnCheckedChangeListener { _, isChecked ->
            skin.conditionMixed = isChecked
        }
        view.detail_skin_improve_acne.setOnCheckedChangeListener { _, isChecked -> skin.improveAcne = isChecked }
        view.detail_skin_improve_sensitivity.setOnCheckedChangeListener { _, isChecked ->
            skin.improveSensitivity = isChecked
        }
        view.detail_skin_improve_wrinkles.setOnCheckedChangeListener { _, isChecked ->
            skin.improveWrinkles = isChecked
        }
        view.detail_skin_improve_pores.setOnCheckedChangeListener { _, isChecked -> skin.improvePores = isChecked }
        view.detail_skin_improve_spots.setOnCheckedChangeListener { _, isChecked -> skin.improveSpots = isChecked }
        view.detail_skin_improve_dull.setOnCheckedChangeListener { _, isChecked -> skin.improveDull = isChecked }
        view.detail_skin_improve_pock.setOnCheckedChangeListener { _, isChecked -> skin.improvePock = isChecked }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.detail_skin_data.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(), R.id.nav_master_controller)
            val action = SkinFragmentDirections.actionNavSkinToNavSkinData(skin)
            controller.navigate(action)
        }
        view.detail_skin_record.setOnClickListener {
            val controller = Navigation.findNavController(requireActivity(), R.id.nav_master_controller)
            val action = SkinFragmentDirections.actionNavSkinToNavSkinRecord(skin)
            controller.navigate(action)
        }

        view.detail_ok.setOnClickListener {
            setString(view.detail_skin_improve_other) { skin.improveOther = it }
            skin.dirty = true

            viewModel.modify(skin)

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
