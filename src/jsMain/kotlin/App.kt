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

val App = FC<Props> {props ->
    var headlines by useState(emptyList<Headline>())
    var filterOnlyResults = true

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
//                if (!filterOnlyResults) {
                    headlines = getHeadlines()
//                }
                headlines = headlines.filter { it.text.contains(input, true) }
            }
//            val cartItem = ShoppingListItem(input.replace("!", ""), input.count { it == '!' })
//            scope.launch {
//                addShoppingListItem(cartItem)
//                shoppingList = getShoppingList()
//            }
        }
    }
//    div {
//        +"Filter only results"
//        input {
//            type = InputType.checkbox
//            onChange = {
//                checked = !filterOnlyResults
//                filterOnlyResults = !filterOnlyResults
//            }
////            checked = true
//        }
//    }
    ol {
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