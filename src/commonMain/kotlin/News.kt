import kotlinx.serialization.Serializable

@Serializable
data class News(
    val title: String,
    val url: String,
    val provider: String = "",
    val overline: String = "",
    val teaser: String = "",
    val text: String = "",
    val breadcrumbs: List<String> = emptyList(),
    val author: String = ""
) {
    fun contains(searchText: String): Boolean {
        //TODO improve toString method
        return this.toString().contains(searchText, true)
    }

    val id: Int = hashCode()

    companion object {
        const val path = "/news"
    }
}