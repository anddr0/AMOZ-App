package com.example.amoz.extensions

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

inline fun <reified T> JsonElement.tryParse(): T? {
    return try {
        Json.decodeFromJsonElement<T>(this)
    } catch (e: SerializationException) {
        null
    }
}