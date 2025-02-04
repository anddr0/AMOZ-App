package com.example.validation.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Digits(
    val integer: Int,
    val fraction: Int,
    val nameOfField: String = ""
)
