package ml.sky233.choseki.sqlite.bean

data class Love(
    val id: Int,
    val name: String,
    val description: String,
    val data: String,
    val packageIds: ArrayList<Int>
)
