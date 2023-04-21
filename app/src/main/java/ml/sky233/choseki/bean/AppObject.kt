package ml.sky233.choseki.bean

class AppObject(title: String, icon: Int) {
    var title = ""
    var icon = 0

    init {
        this.title = title
        this.icon = icon
    }

    override fun toString(): String {
        return "title:$title  tag:$icon"
    }
}