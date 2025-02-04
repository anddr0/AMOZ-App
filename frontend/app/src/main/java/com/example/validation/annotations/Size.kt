package com.example.validation.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Size(val min: Int = 0, val max: Int = Integer.MAX_VALUE, val nameOfField: String = "")