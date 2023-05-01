package ml.sky233.choseki.sqlite.bean

data class Audio(
    val id: Int,
    val name: String,
    val size: Int,
    val duration: Int,
    val packageId: Int
)
