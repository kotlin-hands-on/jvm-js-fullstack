import kotlinx.serialization.Serializable

@Serializable
data class Headline(val text: String, val url: String?, val provider: String?) {
    val id: Int = hashCode()

    companion object {
        const val path = "/headlines"
    }
}