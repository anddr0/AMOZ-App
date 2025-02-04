package com.example.amoz.api.repositories

import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.extensions.toImageBitmap
import com.example.amoz.models.Company
import com.example.amoz.api.requests.CompanyCreateRequest
import com.example.amoz.api.services.CompanyService
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    private val companyService: CompanyService
) : BaseRepository() {

    suspend fun createCompany(request: CompanyCreateRequest): Company? {
        return performRequest {
            companyService.createCompany(request)
        }
    }

    suspend fun getUserCompany(): Company? {
        return performRequest {
            companyService.getUserCompany()
        }
    }

    suspend fun updateCompany(request: CompanyCreateRequest): Company? {
        return performRequest {
            companyService.updateCompany(request)
        }
    }

    suspend fun deactivateCompany() {
        performRequest {
            companyService.deactivateCompany()
        }
    }

    suspend fun uploadCompanyProfilePicture(file: MultipartBody.Part) {
        performRequest {
            companyService.uploadCompanyProfilePicture(file)
        }
    }

    suspend fun getCompanyProfilePicture(): ImageBitmap? {
        return performRequest {
            companyService.getCompanyProfilePicture()
        }?.toImageBitmap()
    }
}
