import web.dom.document
import react.create
import react.dom.client.createRoot
import web.dom.ElementId

fun main() {
    val rootId = ElementId("root")
    val container = document.getElementById(rootId) ?: error("Couldn't find container!")
    createRoot(container).render(App.create())
}