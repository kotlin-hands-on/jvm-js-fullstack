import web.dom.document
import react.create
import react.dom.client.createRoot

fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find container!")
    createRoot(container).render(App.create())
}