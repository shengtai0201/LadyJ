package com.driveinto.ladyj.skin.data

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
import com.driveinto.ladyj.skin.Skin
import kotlinx.android.synthetic.main.fragment_skin_data_detail.view.*

class SkinDataDetailFragment : AbstractFragment() {

    companion object {
        fun newInstance(skin: Skin, skinData: SkinData?, operationValue: Int) =
            SkinDataDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Skin.key, skin)
                    putParcelable(SkinData.key, skinData)
                    putInt(DetailOperations.key, operationValue)
                }
            }
    }

    private val viewModel: SkinDataViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return SkinDataViewModel(activity!!.application) as T
            }
        }
    }

    private lateinit var skin: Skin
    private lateinit var skinData: SkinData
    private lateinit var detailOperation: DetailOperations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            skin = if (it.containsKey(Skin.key)) {
                it.getParcelable(Skin.key)!!
            } else {
                val args = SkinDataDetailFragmentArgs.fromBundle(it)
                args.skin
            }

            val skinData = if (it.containsKey(SkinData.key)) {
                it.getParcelable(SkinData.key)
            } else {
                val args = SkinDataDetailFragmentArgs.fromBundle(it)
                args.skinData
            }
            if (skinData == null) {
                this.skinData = SkinData.emptyInstance(skin.customerId)
            } else {
                this.skinData = skinData
            }

            val operationValue = if (it.containsKey(DetailOperations.key)) {
                it.getInt(DetailOperations.key)
            } else {
                val args = SkinDataDetailFragmentArgs.fromBundle(it)
                args.operationValue
            }
            detailOperation = DetailOperations.fromValue(operationValue)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_skin_data_detail, container, false)

        setText(view.skin_data_advice, skinData.advice)
        setText(view.skin_data_details, skinData.details)

        when (detailOperation) {
            DetailOperations.Create -> view.detail_ok.text = getString(R.string.detail_create)
            DetailOperations.Update -> view.detail_ok.text = getString(R.string.detail_update)
            DetailOperations.Destroy -> {
                view.skin_data_advice.isEnabled = false
                view.skin_data_details.isEnabled = false

                view.detail_ok.text = getString(R.string.detail_destroy)
            }
            else -> view.detail_ok.text = getString(R.string.detail_ok)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.detail_ok.setOnClickListener {
            setString(view.skin_data_advice) { skinData.advice = it }
            setString(view.skin_data_details) { skinData.details = it }
            skinData.dirty = true

            when (detailOperation) {
                DetailOperations.Create -> viewModel.insert(skin, skinData)
                DetailOperations.Update -> viewModel.update(skinData)
                DetailOperations.Destroy -> viewModel.delete(skinData)
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
