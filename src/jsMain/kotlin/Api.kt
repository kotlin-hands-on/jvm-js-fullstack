import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun getShoppingList(): List<ShoppingListItem> {
    return jsonClient.get(ShoppingListItem.path).body()
}

suspend fun addShoppingListItem(shoppingListItem: ShoppingListItem) {
    jsonClient.post(ShoppingListItem.path) {
        contentType(ContentType.Application.Json)
        setBody(shoppingListItem)
    }
}

suspend fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
    jsonClient.delete(ShoppingListItem.path + "/${shoppingListItem.id}")
}

suspend fun getHeadlines(): List<Headline> {
    return jsonClient.get(Headline.path).body()
}

suspend fun filterResults(filterText: String): List<Headline> {
    return jsonClient.get(Headline.path + "/${filterText}").body()
}
