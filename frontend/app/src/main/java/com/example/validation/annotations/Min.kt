package com.example.validation.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Min(
    val value: Int,
    val nameOfField: String = ""
)
