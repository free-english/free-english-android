package io.github.freeenglish.data.sync

import com.squareup.moshi.Json

data class Definition(
    @Json(name = "meaning")
    var meaning: String,
    @Json(name = "examples")
    var examples: List<String>
)