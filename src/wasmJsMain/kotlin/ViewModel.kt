import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

@OptIn(DelicateCoroutinesApi::class)
class ShoppingListViewModel {
    var shoppingList by mutableStateOf(listOf<ShoppingListItem>())
        private set

    fun fetchShoppingList() {
        GlobalScope.promise {
            shoppingList = getShoppingList()
        }
    }

    fun pushShoppingListItem(shoppingListItem: ShoppingListItem) {
        GlobalScope.promise {
            addShoppingListItem(shoppingListItem)
            shoppingList += shoppingListItem
        }
    }

    fun removeShoppingListItem(shoppingListItem: ShoppingListItem) {
        GlobalScope.promise {
            deleteShoppingListItem(shoppingListItem)
            shoppingList -= shoppingListItem
        }
    }
}