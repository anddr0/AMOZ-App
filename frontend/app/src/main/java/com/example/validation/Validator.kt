package com.example.validation

import android.util.Log
import android.util.Patterns
import com.example.amoz.api.requests.AttributeCreateRequest
import com.example.validation.annotations.DecimalMin
import com.example.validation.annotations.Digits
import com.example.validation.annotations.Email
import com.example.validation.annotations.ListSize
import com.example.validation.annotations.Min
import com.example.validation.annotations.NoDublicateAttributes
import com.example.validation.annotations.NotBlank
import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.Past
import com.example.validation.annotations.PastOrPresent
import com.example.validation.annotations.Phone
import com.example.validation.annotations.Positive
import com.example.validation.annotations.Size
import java.lang.reflect.Field
import java.math.BigDecimal
import java.time.LocalDate

object Validator {
    fun validateMin(obj: Any): String? {
        return validateTemplate(obj, Min::class.java) { field, annotation ->
            val value = field.get(obj) as? Int

            if (value != null) {
                val fieldName = annotation.nameOfField.ifBlank { field.name }

                if (value < annotation.value) {
                    return@validateTemplate "'${fieldName}' value has to be more " +
                            "or equal to ${annotation.value}."
                }
            }

            return@validateTemplate null
        }
    }

    fun validateListSize(obj: Any): String? {
        return validateTemplate(obj, ListSize::class.java) { field, annotation ->
            val value = field.get(obj) as? List<*>

            if (value != null) {
                val fieldName = if (annotation.nameOfField.isBlank()) {
                    field.name
                } else {
                    annotation.nameOfField
                }

                if (value.size < annotation.min) {
                    return@validateTemplate "'${fieldName}' list has to contain at least " +
                            "${annotation.min} elements."
                }
            }

            return@validateTemplate null
        }
    }

    fun validateDigits(obj: Any): String? {
        return validateTemplate(obj, Digits::class.java) { field, annotation ->
            val value = field.get(obj) as? BigDecimal

            if (value != null) {
                val integerPart = value.toBigInteger().toString().length
                val fractionPart = value.scale()

                if (integerPart > annotation.integer || fractionPart > annotation.fraction) {
                    val fieldName = if (annotation.nameOfField.isBlank()) {
                        field.name
                    } else {
                        annotation.nameOfField
                    }

                    return@validateTemplate "Field '${fieldName}' must have a value with a maximum of  ${annotation.integer} integer digits i " +
                            "${annotation.fraction} decimal places."
                }
            }

            return@validateTemplate null
        }
    }


    fun validateDecimalMin(obj: Any): String? {
        return validateTemplate(obj, DecimalMin::class.java) { field, annotation ->
            val value = field.get(obj) as? BigDecimal

            if (value != null) {
                val minValue = BigDecimal(annotation.value)
                if ((annotation.inclusive && value <= minValue) || (!annotation.inclusive && value < minValue)) {
                    val fieldName = if (annotation.nameOfField.isBlank()) {
                        field.name
                    } else {
                        annotation.nameOfField
                    }

                    return@validateTemplate "Field '${fieldName}' has to be lover than ${annotation.value}."
                }
            }
            return@validateTemplate null
        }
    }


    fun validatePastOrPresent(obj: Any): String? {
        return validateTemplate(obj, PastOrPresent::class.java) { field, annotation ->
            val value = field.get(obj)
            if (value is LocalDate) {
                val currentDate = LocalDate.now()
                if (value.isAfter(currentDate)) {
                    val fieldName = if (annotation.nameOfField.isBlank()) {
                        field.name
                    } else {
                        annotation.nameOfField
                    }

                    return@validateTemplate "Field '${fieldName}' has to be a date in the past"
                }
            }
            return@validateTemplate null
        }
    }

    fun validatePast(obj: Any): String? {
        return validateTemplate(obj, Past::class.java) { field, annotation ->
            val value = field.get(obj)
            if (value is LocalDate) {
                val currentDate = LocalDate.now()
                if (value.isAfter(currentDate) || value.isEqual(currentDate)) {
                    val fieldName = if (annotation.nameOfField.isBlank()) {
                        field.name
                    } else {
                        annotation.nameOfField
                    }

                    return@validateTemplate "Field '${fieldName}' has to be a date in the past"
                }
            }
            return@validateTemplate null
        }
    }

    fun validatePositive(obj: Any): String? {
        return validateTemplate(obj, Positive::class.java) { field, annotation ->
            val value = field.get(obj) as? Number
            if (value == null || value.toDouble() <= 0) {
                val fieldName = if (annotation.nameOfField.isBlank()) {
                    field.name
                } else {
                    annotation.nameOfField
                }

                return@validateTemplate "Field '${fieldName}' has to be positive"
            }
            return@validateTemplate null
        }
    }

    fun validateNotNullable(obj: Any): String? {
        return validateTemplate(obj, NotNullable::class.java) { field, annotation ->
            val value = field.get(obj)
            if (value == null) {
                val fieldName = if (annotation.nameOfField.isBlank()) {
                    field.name
                } else {
                    annotation.nameOfField
                }

                return@validateTemplate "Field '${fieldName}' can't  be empty"
            }
            return@validateTemplate null
        }
    }


    fun validateEmail(obj: Any): String? {
        return validateTemplate(obj, Email::class.java) { field, annotation ->
            val value = field.get(obj) as? String?
            if (value != null) {
                val fieldName = if (annotation.nameOfField.isBlank()) {
                    field.name
                } else {
                    annotation.nameOfField
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                    return@validateTemplate "Field '${fieldName}' is not a valid email address"
                }
            }
            return@validateTemplate null
        }
    }

    fun validatePhoneNumber(obj: Any): String? {
        return validateTemplate(obj, Phone::class.java) { field, annotation ->
            val value = field.get(obj) as? String?
            if (value != null) {
                val fieldName = if (annotation.nameOfField.isBlank()) { field.name }
                else { annotation.nameOfField }

                if (!Patterns.PHONE.matcher(value).matches()) {
                    return@validateTemplate "Field '${fieldName}' does not match phone pattern"
                }
            }
            return@validateTemplate null
        }
    }

    fun validateSize(obj: Any): String? {
        return validateTemplate(obj, Size::class.java) { field, annotation ->
            val value = field.get(obj) as? String?
            if (value != null) {
                val fieldName = annotation.nameOfField.ifBlank { field.name }

                if (value.length > annotation.max) {
                    return@validateTemplate "Field '${fieldName}' exceeds the maximum length: ${annotation.max}"
                } else if (value.length < annotation.min) {
                    return@validateTemplate "Field '${fieldName}' has no minimum length: ${annotation.min}"
                }
            }
            return@validateTemplate null
        }
    }

    fun validateNoDublicateAttributes(obj: Any): String? {
        return validateTemplate(obj, NoDublicateAttributes::class.java) { field, annotation ->
            val value = field.get(obj) as? List<*>

            if (value != null) {
                val fieldName = if (annotation.nameOfField.isBlank()) {
                    field.name
                } else {
                    annotation.nameOfField
                }

                val attributeRequests = value.filterIsInstance<AttributeCreateRequest>()

                val duplicateAttributeNames = attributeRequests
                    .groupBy { it.attributeName }
                    .filter { it.value.size > 1 }
                    .keys

                if (duplicateAttributeNames.isNotEmpty()) {
                    return@validateTemplate "'${fieldName}' list contains repeating attributes: ${duplicateAttributeNames.joinToString(", ")}."
                }
            }

            return@validateTemplate null
        }
    }


    fun validateNotBlank(obj: Any): String? {
        return validateTemplate(obj, NotBlank::class.java) { field, annotation ->
            val value = field.get(obj) as? String?
            if (value.isNullOrBlank()) {
                val fieldName = annotation.nameOfField.ifBlank { field.name }

                return@validateTemplate "$fieldName should not be blank"
            }
            return@validateTemplate null
        }
    }

    private fun <T : Annotation> validateTemplate(
        obj: Any,
        annotationClass: Class<T>,
        validateField: (Field, T) -> (String?)
    ): String? {
        val validatedFields: MutableList<String?> = mutableListOf()
        val fields = obj::class.java.declaredFields
        for (field in fields) {
            val annotation = field.getAnnotation(annotationClass)
            if (annotation != null) {
                field.isAccessible = true
                Log.i("ValidationLogger", "Validating field: ${field.name}, annotation: ${annotation}")
                validatedFields.add(validateField(field, annotation))
            }
        }
        val violations = validatedFields.filterNotNull()
        return if (violations.isEmpty()) {
            null
        } else {
            violations.first()
        }
    }


}
