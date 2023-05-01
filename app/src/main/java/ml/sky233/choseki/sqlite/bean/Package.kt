package ml.sky233.choseki.sqlite.bean

data class Package(
    val id: Int,
    val name: String,
    val packageId: Int,
    val version: Int,
    val icon: String,
    val isEnable: Boolean
)
