package com.example.amoz.api

import android.content.Context
import com.example.amoz.R
import com.example.amoz.api.managers.FirebaseManager
import com.example.amoz.api.managers.GoogleAuthManager
import com.example.amoz.api.networking.AuthInterceptor
import com.example.amoz.api.managers.TokenManager
import com.example.amoz.api.repositories.AttributeRepository
import com.example.amoz.api.repositories.AuthenticationRepository
import com.example.amoz.api.repositories.CategoryRepository
import com.example.amoz.api.repositories.CompanyRepository
import com.example.amoz.api.repositories.CustomerRepository
import com.example.amoz.api.repositories.EmployeeRepository
import com.example.amoz.api.repositories.ProductOrderRepository
import com.example.amoz.api.repositories.ProductRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.api.repositories.UserRepository
import com.example.amoz.api.services.AttributeService
import com.example.amoz.api.services.AuthenticationService
import com.example.amoz.api.services.CategoryService
import com.example.amoz.api.services.CompanyService
import com.example.amoz.api.services.CustomerService
import com.example.amoz.api.services.EmployeeService
import com.example.amoz.api.services.ProductOrderService
import com.example.amoz.api.services.ProductService
import com.example.amoz.api.services.ProductVariantService
import com.example.amoz.api.services.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Named("authenticatedRetrofit")
    fun provideAuthenticatedRetrofit(interceptor: AuthInterceptor, @ApplicationContext context: Context): Retrofit {
        val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager,
                               authenticationRepository: AuthenticationRepository,
                               googleAuthManager: GoogleAuthManager
    ): AuthInterceptor {
        return AuthInterceptor(tokenManager, authenticationRepository, googleAuthManager)
    }

    @Provides
    @Named("unauthenticatedRetrofit")
    fun provideUnauthenticatedRetrofit(@ApplicationContext context: Context): Retrofit {
        val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
        }

        val client = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    fun provideAttributeService(@Named("authenticatedRetrofit") retrofit: Retrofit): AttributeService {
        return retrofit.create(AttributeService::class.java)
    }

    @Provides
    fun provideAuthenticationService(@Named("unauthenticatedRetrofit") retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    fun provideCategoryService(@Named("authenticatedRetrofit") retrofit: Retrofit): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }

    @Provides
    fun provideCompanyService(@Named("authenticatedRetrofit") retrofit: Retrofit): CompanyService {
        return retrofit.create(CompanyService::class.java)
    }

    @Provides
    fun provideCustomerService(@Named("authenticatedRetrofit") retrofit: Retrofit): CustomerService {
        return retrofit.create(CustomerService::class.java)
    }

    @Provides
    fun provideEmployeeService(@Named("authenticatedRetrofit") retrofit: Retrofit): EmployeeService {
        return retrofit.create(EmployeeService::class.java)
    }

    @Provides
    fun provideProductOrderService(@Named("authenticatedRetrofit") retrofit: Retrofit): ProductOrderService {
        return retrofit.create(ProductOrderService::class.java)
    }

    @Provides
    fun provideProductService(@Named("authenticatedRetrofit") retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }

    @Provides
    fun provideProductVariantService(@Named("authenticatedRetrofit") retrofit: Retrofit): ProductVariantService {
        return retrofit.create(ProductVariantService::class.java)
    }

    @Provides
    fun provideUserService(@Named("authenticatedRetrofit") retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun provideUserRepository(userService: UserService, firebaseManager: FirebaseManager): UserRepository {
        return UserRepository(firebaseManager, userService)
    }

    @Provides
    fun provideAuthenticationRepository(authenticationService: AuthenticationService): AuthenticationRepository {
        return AuthenticationRepository(authenticationService)
    }

    @Provides
    fun provideAttributeRepository(attributeService: AttributeService): AttributeRepository {
        return AttributeRepository(attributeService)
    }

    @Provides
    fun provideCategoryRepository(categoryService: CategoryService): CategoryRepository {
        return CategoryRepository(categoryService)
    }

    @Provides
    fun provideCompanyRepository(companyService: CompanyService): CompanyRepository {
        return CompanyRepository(companyService)
    }

    @Provides
    fun provideCustomerRepository(customerService: CustomerService): CustomerRepository {
        return CustomerRepository(customerService)
    }

    @Provides
    fun provideEmployeeRepository(employeeService: EmployeeService): EmployeeRepository {
        return EmployeeRepository(employeeService)
    }

    @Provides
    fun provideProductOrderRepository(productOrderService: ProductOrderService): ProductOrderRepository {
        return ProductOrderRepository(productOrderService)
    }

    @Provides
    fun provideProductRepository(productService: ProductService): ProductRepository {
        return ProductRepository(productService)
    }

    @Provides
    fun provideProductVariantRepository(productVariantService: ProductVariantService): ProductVariantRepository {
        return ProductVariantRepository(productVariantService)
    }

}
