import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

@OptIn(DelicateCoroutinesApi::class)
class ShoppingListViewModel {
    private val jsonClient = HttpClient()

    var shoppingList by mutableStateOf(listOf<ShoppingListItem>())
        private set

    fun fetchShoppingList() {
        GlobalScope.promise {
            shoppingList = jsonClient.get(ShoppingListItem.path)
        }
    }

    fun addShoppingListItem(shoppingListItem: ShoppingListItem) {
        GlobalScope.promise {
            jsonClient.post(ShoppingListItem.path, shoppingListItem)
            shoppingList += shoppingListItem
        }
    }

    fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
        GlobalScope.promise {
            jsonClient.delete(ShoppingListItem.path + "/${shoppingListItem.id}")
            shoppingList -= shoppingListItem
        }
    }
}