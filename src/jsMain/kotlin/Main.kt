import react.dom.render
import kotlinx.browser.document

fun main() {
    render(document.getElementById("root")) {
        child(app)
    }
}