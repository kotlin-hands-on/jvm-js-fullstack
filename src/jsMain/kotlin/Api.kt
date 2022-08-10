import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

import kotlinx.browser.window

val endpoint = window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun getShoppingList(): List<ShoppingListItem> {
    return jsonClient.get(endpoint + ShoppingListItem.path).body()
}

suspend fun addShoppingListItem(shoppingListItem: ShoppingListItem) {
    jsonClient.post(endpoint + ShoppingListItem.path) {
        contentType(ContentType.Application.Json)
        setBody(shoppingListItem)
    }
}

suspend fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
    jsonClient.delete(endpoint + ShoppingListItem.path + "/${shoppingListItem.id}")
}