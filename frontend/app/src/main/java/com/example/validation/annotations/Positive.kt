package com.example.validation.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Positive(
    val nameOfField: String = ""
)
