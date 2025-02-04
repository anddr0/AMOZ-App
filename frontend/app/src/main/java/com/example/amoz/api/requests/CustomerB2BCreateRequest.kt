package com.example.amoz.api.requests

import android.provider.Telephony.Mms.Addr
import com.example.validation.annotations.NotBlank
import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.Size
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.CustomerB2B
import kotlinx.serialization.Serializable

@Serializable
data class CustomerB2BCreateRequest(
    @field:NotNullable(nameOfField = "Customer")
    val customer: CustomerCreateRequest = CustomerCreateRequest(),

    @field:NotBlank(nameOfField = "Company number")
    @field:Size(max = 30, nameOfField = "Company number")
    val companyNumber: String = "",

    @field:NotBlank(nameOfField = "Name on invoice")
    @field:Size(max = 255, nameOfField = "Name on invoice")
    val nameOnInvoice: String = "",

    @field:NotNullable(nameOfField = "Address")
    val address: AddressCreateRequest = AddressCreateRequest()
) : ValidatableRequest<CustomerB2BCreateRequest>() {
    constructor(customerB2B: CustomerB2B): this(
        customer = CustomerCreateRequest(customerB2B.customer),
        companyNumber = customerB2B.companyNumber,
        nameOnInvoice = customerB2B.nameOnInvoice,
        address = AddressCreateRequest(customerB2B.address)
    )
}
