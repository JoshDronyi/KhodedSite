package com.probro.khoded.serialization

import kotlinx.serialization.json.Json

val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = true
    prettyPrint = true
}