package io.github.freeenglish.data.sync

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.github.freeenglish.data.entities.Definition

 class Question(
    @Json(name = "name")
    var name: String = ""

)