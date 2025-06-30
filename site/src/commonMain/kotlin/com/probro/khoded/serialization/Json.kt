package com.probro.khoded.utils

import kotlinx.serialization.json.Json

val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = true
    prettyPrint = true
}