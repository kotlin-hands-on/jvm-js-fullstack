import react.*
import react.dom.*
import kotlinx.html.js.*
import kotlinx.coroutines.*

private val scope = MainScope()

val app = fc<Props> {
    var shoppingList by useState(emptyList<ShoppingListItem>())

    useEffectOnce {
        scope.launch {
            shoppingList = getShoppingList()
        }
    }

    h1 {
        +"Full-Stack Shopping List"
    }
    ul {
        shoppingList.sortedByDescending(ShoppingListItem::priority).forEach { item ->
            li {
                key = item.toString()
                attrs.onClickFunction = {
                    scope.launch {
                        deleteShoppingListItem(item)
                        shoppingList = getShoppingList()
                    }
                }
                +"[${item.priority}] ${item.desc} "
            }
        }
    }
    child(inputComponent) {
        attrs.onSubmit = { input ->
            val cartItem = ShoppingListItem(input.replace("!", ""), input.count { it == '!' })
            scope.launch {
                addShoppingListItem(cartItem)
                shoppingList = getShoppingList()
            }
        }
    }
}