import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("ShoppingList") {
        val vm = ShoppingListViewModel()
        ShoppingList(vm)
    }
}