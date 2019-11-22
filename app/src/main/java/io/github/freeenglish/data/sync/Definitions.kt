package io.github.freeenglish.data.sync

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class Definitions(
    @SerializedName("value")
    var value: String,
    @SerializedName("definitions")
    var definition: List<Definition>
) {}