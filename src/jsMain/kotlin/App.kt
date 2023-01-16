import csstype.ClassName
import kotlinx.coroutines.*
import react.*
import react.dom.html.AnchorTarget
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.em
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.strong

private val scope = MainScope()

val App = FC<Props> { props ->
    var news by useState(emptyList<News>())

    useEffectOnce {
        scope.launch {
            news = getNews()
        }
    }
    div {
        className = ClassName("page_title")
        h1 {
            +"Sören's fast Scraper"
        }
        InputComponent {
            onSubmit = { input ->
                scope.launch {
                    news = if (input.isEmpty()) getNews() else filterResults(input)
                }
            }
        }
    }
    div {
        id = "headLineListContainer"
        ol {
            id = "headLineList"
            news.forEach { item ->
                li {
                    a {
                        href = item.url
                        target = AnchorTarget._blank
                        div {
                            +item.date
                        }
                        div {
                            +item.provider
                        }
                        div {
                            strong { +item.overline }
                        }
                        div {
                            em {
                                +item.title
                            }
                        }
                        div {
                            +item.teaser
                        }
                        div {
                            +item.text
                        }
                    }
                }
            }
        }
    }
}
