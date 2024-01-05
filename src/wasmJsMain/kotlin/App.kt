// Reworked https://github.com/russhwolf/To-Do android app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingList(viewModel: ShoppingListViewModel) {
    val shoppingList = viewModel.shoppingList

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingList()
    }

    ShoppingListTheme {
        Surface {
            Column(Modifier.fillMaxSize()) {
                Input(onCreateItem = viewModel::pushShoppingListItem)
                LazyColumn {
                    items(shoppingList.size) { index ->
                        ShoppingItem(
                            shoppingListItem = shoppingList[index],
                            onDeleteClick = viewModel::removeShoppingListItem
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Input(onCreateItem: (ShoppingListItem) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var input by remember { mutableStateOf(TextFieldValue()) }

            fun createShoppingItem() {
                val text = input.text.trim()
                if (text.isBlank()) return
                onCreateItem(ShoppingListItem(text.replace("!", ""), text.count { it == '!' }))
                input = input.copy(text = "")
            }

            OutlinedTextField(
                value = input,
                singleLine = true,
                onValueChange = { input = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { createShoppingItem() }),
                modifier = Modifier.weight(1f).padding(8.dp),
            )
            OutlinedButton(
                onClick = { createShoppingItem() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Create")
            }
        }
        Divider()
    }
}

@Composable
fun ShoppingItem(shoppingListItem: ShoppingListItem, onDeleteClick: (ShoppingListItem) -> Unit) = Column {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("[${shoppingListItem.priority}] ${shoppingListItem.desc}", Modifier.weight(1f))
        IconButton(
            onClick = { onDeleteClick(shoppingListItem) }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
    Divider()
}

@Composable
fun ShoppingListTheme(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = lightColorScheme(
        primary = Color(0xFF3759DF),
        secondary = Color(0xFF375A86)
    ),
    typography = MaterialTheme.typography,
    shapes = MaterialTheme.shapes,
    content = content
)