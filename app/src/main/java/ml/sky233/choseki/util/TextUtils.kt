package ml.sky233.choseki.util

object TextUtils {


    fun String.isUrl(): Boolean {
        val pattern = "^((https?|ftp|file)://)?([a-z0-9-]+\\.)+[a-z0-9]{2,4}.*$"
        val regex = Regex(pattern, RegexOption.IGNORE_CASE)
        return regex.matches(this)
    }

}