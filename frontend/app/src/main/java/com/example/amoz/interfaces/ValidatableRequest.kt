package com.example.amoz.interfaces

import android.util.Log
import com.example.validation.Validator

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


abstract class ValidatableRequest<T : ValidatableRequest<T>>  {
    fun validate(): String? {
        cascadeValidate()?.let { validationErrorMessage ->
            return validationErrorMessage
        }

        val violations: List<String> = listOfNotNull(
            Validator.validateDigits(this),
            Validator.validatePositive(this),
            Validator.validateEmail(this),
            Validator.validateSize(this),
            Validator.validateNotBlank(this),
            Validator.validateMin(this),
            Validator.validatePast(this),
            Validator.validatePastOrPresent(this),
            Validator.validateNotNullable(this),
            Validator.validateDecimalMin(this),
            Validator.validateListSize(this),
            Validator.validateNoDublicateAttributes(this),
            Validator.validatePhoneNumber(this)
        )

        Log.i("ValidationLogger", "Validating class: ${this::class.simpleName}")

        return if (violations.isNotEmpty()) {
            violations.first()
        } else {
            null
        }
    }

    private fun cascadeValidate(): String? {
        val violations: MutableList<String> = mutableListOf()
        this::class.memberProperties.forEach { property ->
            property.isAccessible = true

            @Suppress("UNCHECKED_CAST")
            val value = (property as? KProperty1<Any, Any>)?.get(this)

            when (value) {
                is ValidatableRequest<*> -> {
                    value.validate()?.let { violation ->
                        violations.add(violation)
                    }
                }
                is List<*> -> {
                    value.filterIsInstance<ValidatableRequest<*>>()
                        .forEach {
                            it.validate()?.let { violation ->
                                violations.add(violation)
                            }
                        }
                }
            }
        }
        return if (violations.isNotEmpty()) {
            violations.first()
        } else {
            null
        }
    }
}
