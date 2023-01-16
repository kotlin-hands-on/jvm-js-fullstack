import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import kotlinx.css.Color.Companion.transparent
import kotlinx.css.properties.TextDecoration
import scraper.Scraper

val collection = mutableListOf(
    ShoppingListItem("Cucumbers 🥒", 1),
    ShoppingListItem("Tomatoes 🍅", 2),
    ShoppingListItem("Orange Juice 🍊", 3)
)

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9090
    val scraper = Scraper()
    scraper.getNews()
    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }

        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }
            get("/styles.css") {
                call.respondCss {
                    body {
                        backgroundColor = Color.lightGray
                        margin(10.px)
                    }
                    rule(".page_title") {
                        color = Color.gray
                        margin(all = LinearDimension.auto)
                        width = LinearDimension("50%")
                    }
                    rule("a") {
                        color = Color.black
                        backgroundColor = transparent
                        textDecoration = TextDecoration.none
                    }
                    rule("li") {
                        padding(5.px)
                    }
                }
            }
            route(ShoppingListItem.path) {
                get {
                    call.respond(collection.toList())
                }
                post {
                    collection.add(call.receive<ShoppingListItem>())
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    collection.removeIf { it.id == id }
                    call.respond(HttpStatusCode.OK)
                }
            }
            route(News.path) {
                get { call.respond(scraper.newsList) }
                post {
                    val filterString = call.receive<String>()
                    scraper.newsList.add(News(filterString, filterString, filterString))
                    call.respond(HttpStatusCode.OK)
                }
                get("/{filterstring}") {
                    call.respond(scraper.filterBy(call.parameters["filterstring"]))
                }
                route("/{filterstring}") {

                }
            }
        }
    }.start(wait = true)
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}