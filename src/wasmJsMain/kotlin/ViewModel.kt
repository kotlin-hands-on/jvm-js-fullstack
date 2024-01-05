import androidx.compose.runtime.mutableStateListOf

class ShoppingListViewModel {
    val shoppingList = mutableStateListOf<ShoppingListItem>()

    fun fetchShoppingList() {
    }

    fun addShoppingListItem(shoppingListItem: ShoppingListItem) {
        shoppingList.add(shoppingListItem)
    }

    fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
        shoppingList.remove(shoppingListItem)
    }
}