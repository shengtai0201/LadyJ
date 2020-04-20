package com.driveinto.ladyj.customer

import android.content.Context
import com.driveinto.ladyj.content.IEnumSpinner
import com.driveinto.ladyj.R

// http://devsourcenter.blogspot.com/2015/09/customising-android-spinner-with-custom.html
enum class CustomerRoles(val value: Int, val id: String, val position: Int) : IEnumSpinner {

    None(1, "5D0E5AA4-CECB-46FC-92A6-709AF39A8968", 0) {
        override fun getText(context: Context): String = context.getString(R.string.customer_roles_none)
    },

    AT(2, "5FE0D62E-93FD-4015-85BC-B9A1B5B2F0F8", 1) {
        override fun getText(context: Context): String = context.getString(R.string.customer_roles_at)
    },

    BrandRepresentative(4, "691938B8-571C-46C0-8F4E-34BC23419E81", 2) {
        override fun getText(context: Context): String = context.getString(R.string.customer_roles_brand_representative)
    },

    VIP(8, "DE078A3B-86D5-4337-BF05-C603D1E23B4B", 3) {
        override fun getText(context: Context): String = context.getString(R.string.customer_roles_vip)
    },

    Retail(16, "69D7CDD0-D63D-4EC5-9E88-055FF0083973", 4) {
        override fun getText(context: Context): String = context.getString(R.string.customer_roles_retail)
    };

    companion object {
        private val values = CustomerRoles.values().associateBy(CustomerRoles::value)
        fun fromValue(value: Int) = values[value]

        private val ids = CustomerRoles.values().associateBy(CustomerRoles::id)
        fun fromId(id: String) = ids[id]

        private val positions = CustomerRoles.values().associateBy(CustomerRoles::position)
        fun fromPosition(position: Int) = positions[position]
    }
}