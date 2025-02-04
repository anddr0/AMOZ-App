package com.example.amoz.api.requests

import com.example.validation.annotations.NotBlank
import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.Size
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Company
import kotlinx.serialization.Serializable


@Serializable
data class CompanyCreateRequest(

    @field:NotBlank(nameOfField = "Company number")
    @field:Size(max = 50, nameOfField = "Company number")
    var companyNumber: String = "",

    @field:NotBlank(nameOfField = "Country of registration")
    @field:Size(max = 100, nameOfField = "Country of registration")
    var countryOfRegistration: String = "",

    @field:NotBlank(nameOfField = "Company name")
    @field:Size(max = 100, nameOfField = "Company name")
    var name: String = "",

    @field:NotNullable(nameOfField = "Address")
    var address: AddressCreateRequest = AddressCreateRequest(),

    @field:Size(max = 14, nameOfField = "REGON")
    var regon: String? = null
) : ValidatableRequest<CompanyCreateRequest>() {
    constructor(company: Company) : this(
        companyNumber = company.companyNumber,
        countryOfRegistration = company.countryOfRegistration,
        name = company.name,
        address = AddressCreateRequest(company.address),
        regon = company.regon
    )
}


