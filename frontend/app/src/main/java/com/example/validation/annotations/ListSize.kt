package com.example.validation.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ListSize(
    val min: Int = 0,
    val nameOfField: String = ""
)
