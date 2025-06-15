import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun getShoppingList(): List<ShoppingListItem> {
    return client.get(ShoppingListItem.PATH).body()
}

suspend fun addShoppingListItem(shoppingListItem: ShoppingListItem) {
    client.post(ShoppingListItem.PATH) {
        contentType(ContentType.Application.Json)
        setBody(shoppingListItem)
  }
}

suspend fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
    client.delete(ShoppingListItem.PATH + "/${shoppingListItem.id}")
}
