package com.example.amoz.api.sealed

import com.example.amoz.extensions.tryParse
import com.example.amoz.models.CustomerAnyRepresentation
import kotlinx.serialization.json.JsonElement

sealed class CustomerResult {
    data class B2B(val data: com.example.amoz.models.CustomerB2B) : CustomerResult()
    data class B2C(val data: com.example.amoz.models.CustomerB2C) : CustomerResult()

    companion object {
        fun createCustomerDetails(customerJson: JsonElement): CustomerResult {
            val customerB2B = customerJson.tryParse<com.example.amoz.models.CustomerB2B>()
            if (customerB2B != null) {
                return B2B(customerB2B)
            }

            val customerB2C = customerJson.tryParse<com.example.amoz.models.CustomerB2C>()
            if (customerB2C != null) {
                return B2C(customerB2C)
            }
            throw Exception("Invalid customer type")
        }
    }


    fun toCustomerAnyRepresentation(): CustomerAnyRepresentation {
        return when (this) {
            is B2B -> CustomerAnyRepresentation(this.data)
            is B2C -> CustomerAnyRepresentation(this.data)
        }
    }
}



