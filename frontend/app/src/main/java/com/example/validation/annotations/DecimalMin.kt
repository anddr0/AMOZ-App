package com.example.validation.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DecimalMin(
    val value: String,
    val inclusive: Boolean = false,
    val nameOfField: String = ""
)
