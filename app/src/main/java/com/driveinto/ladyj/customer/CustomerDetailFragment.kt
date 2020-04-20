package com.driveinto.ladyj.customer

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation.findNavController
import com.driveinto.ladyj.DetailOperations

import com.driveinto.ladyj.R
import com.driveinto.ladyj.app.DatePickerFragment
import com.driveinto.ladyj.app.AbstractFragment
import com.driveinto.ladyj.login.LoginResult
import com.driveinto.ladyj.room.Converters
import kotlinx.android.synthetic.main.fragment_customer_detail.view.*

class CustomerDetailFragment : AbstractFragment() {

    companion object {
        fun newInstance(loginResult: LoginResult, customer: Customer?, operationValue: Int) =
            CustomerDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(LoginResult.nameKey, loginResult)
                    putParcelable(Customer.key, customer)
                    putInt(DetailOperations.key, operationValue)
                }
            }
    }

    private val viewModel: CustomerViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

                @Suppress("UNCHECKED_CAST")
                return CustomerViewModel(activity!!.application) as T
            }
        }
    }

    private lateinit var loginResult: LoginResult
    private lateinit var customer: Customer
    private lateinit var detailOperation: DetailOperations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            loginResult = if (it.containsKey(LoginResult.nameKey)) {
                it.getParcelable(LoginResult.nameKey)!!
            } else {
                val args = CustomerDetailFragmentArgs.fromBundle(it)
                args.loginResult
            }

            val customer = if (it.containsKey(Customer.key)) {
                it.getParcelable(Customer.key)
            } else {
                val args = CustomerDetailFragmentArgs.fromBundle(it)
                args.customer
            }
            if (customer == null) {
                this.customer = Customer.emptyInstance()
            } else {
                this.customer = customer
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
        val view = inflater.inflate(R.layout.fragment_customer_detail, container, false)

        // 設置下拉選單
        ArrayAdapter.createFromResource(requireContext(), R.array.customer_roles, android.R.layout.simple_spinner_item)
            .also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                view.customer_role.adapter = it
            }

        if (customer.memberId != null) {
            view.customer_member_id.setText(customer.memberId)
        }
        view.customer_name.setText(customer.name)
        if (customer.phone != null) {
            view.customer_phone.setText(customer.phone)
        }
        view.customer_birthday.text = Converters.toDateString(customer.birthdayMillis)
        view.customer_address.setText(customer.address)
        if (customer.job != null) {
            view.customer_job.setText(customer.job)
        }
        if (customer.lineId != null) {
            view.customer_line_id.setText(customer.lineId)
        }
        if (customer.dateMillis != null) {
            view.customer_date.text = Converters.toDateString(customer.dateMillis!!)
        }
        if (customer.height != null) {
            view.customer_height.setText(customer.height.toString())
        }
        if (customer.weight != null) {
            view.customer_weight.setText(customer.weight.toString())
        }
        view.customer_reference.setText(customer.reference)
        view.customer_role.setSelection(customer.getRolePosition())

        when (detailOperation) {
            DetailOperations.Create -> view.detail_ok.text = getString(R.string.detail_create)
            DetailOperations.Update -> {
                view.detail.visibility = View.VISIBLE

                view.detail_ok.text = getString(R.string.detail_update)
            }
            DetailOperations.Destroy -> {
                view.customer_member_id.isEnabled = false
                view.customer_name.isEnabled = false
                view.customer_phone.isEnabled = false
                view.customer_birthday.isEnabled = false
                view.customer_address.isEnabled = false
                view.customer_job.isEnabled = false
                view.customer_line_id.isEnabled = false
                view.customer_date.isEnabled = false
                view.customer_height.isEnabled = false
                view.customer_weight.isEnabled = false
                view.customer_reference.isEnabled = false
                view.customer_role.isEnabled = false

                view.detail_ok.text = getString(R.string.detail_destroy)
            }
            else -> view.detail_ok.text = getString(R.string.detail_ok)
        }

        view.customer_birthday.setOnClickListener {
            DatePickerFragment(DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                customer.birthdayMillis = Converters.toUTCMillis(year, month + 1, dayOfMonth)
                view.customer_birthday.text = getString(R.string.date_format, year, month + 1, dayOfMonth)
            }, customer.birthdayMillis).show(requireActivity().supportFragmentManager, "birthdayPicker")
        }

        view.customer_date.setOnClickListener {
            DatePickerFragment(DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                customer.dateMillis = Converters.toUTCMillis(year, month + 1, dayOfMonth)
                view.customer_date.text = getString(R.string.date_format, year, month + 1, dayOfMonth)
            }, customer.dateMillis).show(requireActivity().supportFragmentManager, "datePicker")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 美體
        viewModel.body.observe(viewLifecycleOwner, Observer {
            val controller = findNavController(requireActivity(), R.id.nav_master_controller)
            val action = if (resources.getBoolean(R.bool.twoPane)) {
                CustomerFragmentDirections.actionNavCustomerToNavBody(it)
            } else {
                CustomerDetailFragmentDirections.actionNavCustomerDetailToNavBody(it)
            }
            controller.navigate(action)
        })
        view.detail_body.setOnClickListener {
            viewModel.readBody(customer)
        }

        // 美膚
        viewModel.skin.observe(viewLifecycleOwner, Observer {
            val controller = findNavController(requireActivity(), R.id.nav_master_controller)
            val action = if (resources.getBoolean(R.bool.twoPane)) {
                CustomerFragmentDirections.actionNavCustomerToNavSkin(it)
            } else {
                CustomerDetailFragmentDirections.actionNavCustomerDetailToNavSkin(it)
            }
            controller.navigate(action)
        })
        view.detail_skin.setOnClickListener {
            viewModel.readSkin(customer)
        }

        view.detail_ok.setOnClickListener {
            customer.memberId = view.customer_member_id.text.toString()
            customer.name = view.customer_name.text.toString()
            customer.phone = view.customer_phone.text.toString()
            customer.address = view.customer_address.text.toString()
            customer.job = view.customer_job.text.toString()
            customer.lineId = view.customer_line_id.text.toString()
            customer.height = view.customer_height.text.toString().toInt()
            customer.weight = view.customer_weight.text.toString().toInt()
            customer.reference = view.customer_reference.text.toString()
            customer.dirty = true

            val role = CustomerRoles.fromPosition(view.customer_role.selectedItemPosition)
            if (role == null) {
                customer.roleId = CustomerRoles.None.id
            } else {
                customer.roleId = role.id
            }

            when (detailOperation) {
                DetailOperations.Create -> viewModel.insert(loginResult, customer)
                DetailOperations.Update -> viewModel.update(customer)
                DetailOperations.Destroy -> viewModel.delete(customer)
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
            val controller = findNavController(requireActivity(), R.id.nav_master_controller)
            controller.navigateUp()
        }
    }
}
