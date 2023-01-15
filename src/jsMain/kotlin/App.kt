import kotlinx.browser.document
import kotlinx.coroutines.*
import org.w3c.dom.HTMLFormElement
import react.*
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol

private val scope = MainScope()

val App = FC<Props> { props ->
    var headlines by useState(emptyList<Headline>())

    useEffectOnce {
        scope.launch {
            headlines = getHeadlines()
        }
    }

    h1 {
        +"Sören's fast Scraper"
    }

    InputComponent {
        onSubmit = { input ->
            scope.launch {
                headlines = if (input.isEmpty()) getHeadlines() else filterResults(input)
            }
        }
    }
    div {
        id = "headLineListContainer"
        ol {
            id = "headLineList"
            headlines.forEach { item ->
                li {
                    a {
                        href = item.url
                        +"${item.text} (${item.provider})"
                    }
                }
            }
        }
    }
}