package com.example.amoz.api.repositories

import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.extensions.toImageBitmap
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.services.ProductVariantService
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.util.UUID
import javax.inject.Inject

class ProductVariantRepository @Inject constructor(
    private val productVariantService: ProductVariantService
) : BaseRepository() {

    suspend fun createProductVariant(request: ProductVariantCreateRequest): ProductVariantDetails? {
        return performRequest {
            productVariantService.createProductVariant(request)
        }
    }

    suspend fun updateProductVariant(productVariantId: UUID, request: ProductVariantCreateRequest): ProductVariantDetails? {
        return performRequest {
            productVariantService.updateProductVariant(productVariantId, request)
        }
    }

    suspend fun deactivateProductVariant(productVariantId: UUID) {
        performRequest {
            productVariantService.deactivateProductVariant(productVariantId)
        }
    }

    suspend fun getAllProductVariantsByProductId(productId: UUID): List<ProductVariantSummary> {
        return performRequest {
            productVariantService.getAllProductVariantsByProductId(productId)
        } ?: listOf()
    }

    suspend fun getProductVariant(productVariantId: UUID): ProductVariantDetails? {
        return performRequest {
            productVariantService.getProductVariant(productVariantId)
        }
    }

    suspend fun uploadProductVariantPicture(productVariantId: UUID, file: MultipartBody.Part) {
        performRequest {
            productVariantService.uploadProductVariantPicture(productVariantId, file)
        }
    }

    suspend fun getProductVariantPicture(productVariantId: UUID): ImageBitmap? {
        return performRequest {
            productVariantService.getProductVariantPicture(productVariantId)
        }?.toImageBitmap()
    }
}
