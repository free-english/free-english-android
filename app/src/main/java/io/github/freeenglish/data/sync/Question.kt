package io.github.freeenglish.data.sync

import com.squareup.moshi.Json

class Question(
    @Json(name = "name")
    var name: String = ""

)