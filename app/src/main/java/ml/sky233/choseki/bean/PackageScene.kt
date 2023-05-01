package ml.sky233.choseki.bean

import com.google.gson.JsonElement

data class PackageScene(
    val name: String,
    val description: String,
    val data: JsonElement,
)
