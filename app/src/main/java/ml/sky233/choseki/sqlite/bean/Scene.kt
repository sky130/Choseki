package ml.sky233.choseki.sqlite.bean

data class Scene(
    val id: Int?,
    val name: String,
    val description: String,
    val data: String,
    val packageId: Int,
)