package com.david0926.simpletodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.david0926.simpletodo.ui.theme.SimpleTODOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleTODOTheme {
                Main()
            }
        }
    }
}

val Colors.primaryColor: Color
    @Composable
    get() = if (isLight) colorResource(R.color.purple) else Color.White

val Colors.secondaryColor: Color
    @Composable
    get() = if (isLight) Color.White else colorResource(R.color.purple)

@Composable
fun Main() {
    var list: List<Todo> by rememberSaveable { mutableStateOf(listOf()) }

    fun addTodo(todo: Todo) {
        list = list + listOf(todo)
    }

    fun editTodo(i: Int, todo: Todo) {
        list = list.toMutableList().also { it[i] = todo }
    }

    fun deleteTodo(i: Int) {
        list = list.toMutableList().also { it.removeAt(i) }
    }

    val (showDialog, setShowDialog) =
        rememberSaveable { mutableStateOf(false) }
    var deleteItem by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopBar(onAction = { addTodo(Todo()) })
        },
    ) {
        TodoList(
            list,
            onChange = { i, todo -> editTodo(i, todo) },
            onDelete = {
                deleteItem = it
                setShowDialog(true)
            }
        )
        DeleteDialog(showDialog, setShowDialog) {
            deleteTodo(deleteItem)
        }
    }
}

@Composable
fun TopBar(onAction: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.appbar_title)) },
        actions = {
            IconButton(onClick = { onAction() }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_todo)
                )
            }
        })
}

@Composable
fun TodoList(
    todos: List<Todo>,
    onChange: (i: Int, todo: Todo) -> Unit,
    onDelete: (i: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        itemsIndexed(items = todos) { i, todo ->
            TodoItem(
                item = todo,
                onChange = { onChange(i, it) },
                onDelete = { onDelete(i) }
            )
        }
    }
}

@Composable
fun TodoItem(item: Todo, onChange: (todo: Todo) -> Unit, onDelete: () -> Unit) {

    Card(modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth()
        .pointerInput(Unit) {
            detectTapGestures(onLongPress = { onDelete() })
        }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            if (item.onEdit) {
                TextField(
                    value = item.text,
                    onValueChange = { onChange(item.copy(text = it)) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { onChange(item.copy(onEdit = false)) }) {
                    Icon(
                        tint = MaterialTheme.colors.primaryColor,
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(R.string.todo_done)
                    )
                }
            } else {
                Text(
                    text = item.text,
                    textDecoration = if (item.done) TextDecoration.LineThrough else TextDecoration.None,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Checkbox(
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primaryColor),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    checked = item.done,
                    onCheckedChange = { onChange(item.copy(done = it)) })
            }
        }
    }
}

@Composable
fun DeleteDialog(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    onConfirm: () -> Unit
) {
    if (!showDialog) return
    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        title = { Text(text = stringResource(R.string.delete_dialog_title)) },
        text = { Text(text = stringResource(R.string.delete_dialog_text)) },
        confirmButton = {
            Button(
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryColor),
                onClick = {
                    onConfirm()
                    setShowDialog(false)
                }) {
                Text(
                    text = stringResource(R.string.dialog_confirm),
                    color = MaterialTheme.colors.primaryColor
                )
            }
        },
        dismissButton = {
            Button(
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryColor),
                onClick = { setShowDialog(false) }) {
                Text(
                    text = stringResource(R.string.dialog_cancel),
                    color = MaterialTheme.colors.primaryColor
                )
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun Preview() {
    SimpleTODOTheme {
        Main()
    }
}