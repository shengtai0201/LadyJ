package com.driveinto.ladyj.skin.record

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
import com.driveinto.ladyj.customer.CustomerDetailFragmentArgs
import com.driveinto.ladyj.skin.Skin
import kotlinx.android.synthetic.main.fragment_customer_detail.view.*
import kotlinx.android.synthetic.main.fragment_skin_record_detail.view.*
import kotlinx.android.synthetic.main.fragment_skin_record_detail.view.detail_cancel
import kotlinx.android.synthetic.main.fragment_skin_record_detail.view.detail_ok

class SkinRecordDetailFragment : AbstractFragment() {

    companion object {
        fun newInstance(skin: Skin, skinRecord: SkinRecord?, operationValue: Int, authorizationValue: Int) =
            SkinRecordDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Skin.key, skin)
                    putParcelable(SkinRecord.key, skinRecord)
                    putInt(DetailOperations.key, operationValue)
                    putInt(DetailAuthorizations.key, authorizationValue)
                }
            }
    }

    private val viewModel: SkinRecordViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return SkinRecordViewModel(activity!!.application) as T
            }
        }
    }

    private lateinit var skin: Skin
    private lateinit var skinRecord: SkinRecord
    private lateinit var operation: DetailOperations
    private lateinit var authorization: DetailAuthorizations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            skin = if (it.containsKey(Skin.key)) {
                it.getParcelable(Skin.key)!!
            } else {
                val args = SkinRecordDetailFragmentArgs.fromBundle(it)
                args.skin
            }

            val skinRecord = if (it.containsKey(SkinRecord.key)) {
                it.getParcelable(SkinRecord.key)
            } else {
                val args = SkinRecordDetailFragmentArgs.fromBundle(it)
                args.skinRecord
            }
            if (skinRecord == null) {
                this.skinRecord = SkinRecord.emptyInstance(skin.customerId)
            } else {
                this.skinRecord = skinRecord
            }

            val operationValue = if (it.containsKey(DetailOperations.key)) {
                it.getInt(DetailOperations.key)
            } else {
                val args = SkinRecordDetailFragmentArgs.fromBundle(it)
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
        val view = inflater.inflate(R.layout.fragment_skin_record_detail, container, false)

        setText(view.skin_record_remark, skinRecord.remark)

        // UI 控制
        if (authorization == DetailAuthorizations.ReadOnly) {
            view.skin_record_remark.isEnabled = false

            view.detail_ok.visibility = View.GONE
        } else {
            when (operation) {
                DetailOperations.Create -> view.detail_ok.text = getString(R.string.detail_create)
                DetailOperations.Update -> view.detail_ok.text = getString(R.string.detail_update)
                DetailOperations.Destroy -> {
                    view.skin_record_remark.isEnabled = false

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
            setString(view.skin_record_remark) { skinRecord.remark = it }
            skinRecord.dirty = true

            when (operation) {
                DetailOperations.Create -> viewModel.insert(skin, skinRecord)
                DetailOperations.Update -> viewModel.update(skinRecord)
                DetailOperations.Destroy -> viewModel.delete(skinRecord)
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
