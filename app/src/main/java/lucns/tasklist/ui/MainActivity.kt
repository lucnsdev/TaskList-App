package lucns.tasklist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.codelab.basics.ui.theme.BasicsAppTheme
import kotlinx.coroutines.launch
import lucns.tasklist.R
import lucns.tasklist.TaskApplication
import lucns.tasklist.models.Task
import lucns.tasklist.repositories.AppDataContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backgroundColor = ContextCompat.getColor(this, R.color.app_background)
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.dark(backgroundColor))

        setContent {
            val backgroundColor = colorResource(R.color.app_background).toArgb()
            SideEffect {
                enableEdgeToEdge(navigationBarStyle = SystemBarStyle.dark(backgroundColor))
            }
            BasicsAppTheme {
                MyApp()
            }
        }
    }

    @Composable
    fun MyApp() {
        val context = LocalContext.current
        val application = LocalContext.current.applicationContext as TaskApplication
        val scope = rememberCoroutineScope()
        val repository = AppDataContainer(context).taskRepository
        val tasksState by application.container.taskRepository.getAllItems().collectAsState(initial = null)
        val dialogs = DialogsTask()
        var showAddTaskDialog by rememberSaveable { mutableStateOf(false) }
        Scaffold(
            topBar = {
                CustomTopBar(
                    onBackClick = {
                        (context as? Activity)?.finish()
                    },
                    onSearchClick = {
                        Toast.makeText(context, ContextCompat.getString(context, R.string.not_implemented), Toast.LENGTH_SHORT).show()
                    },
                    onMoreClick = {
                        Toast.makeText(context, ContextCompat.getString(context, R.string.not_implemented), Toast.LENGTH_SHORT).show()
                    }
                )
            }, floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    containerColor = colorResource(R.color.accent),
                    onClick = {
                        showAddTaskDialog = true
                    }) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "fab", tint = Color.White)
                }
            }, content = { innerPadding ->
                val currentTasks = tasksState
                Box(modifier = Modifier.padding(innerPadding)) {
                    when {
                        currentTasks == null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .background(colorResource(R.color.accent))
                                        .padding(16.dp)
                                )
                            }
                        }

                        currentTasks.isEmpty() -> {
                            EmptyScreen()
                        }

                        else -> {
                            CustomCards(tasks = currentTasks)
                        }
                    }
                }
            }
        )
        if (showAddTaskDialog) {
            dialogs.ShowDialogRegisterTask(initialTile = null, initialDescription = null, onCanceled = {
                showAddTaskDialog = false
            }, onConfirmed = { title, description ->
                run {
                    showAddTaskDialog = false
                    if (title.isEmpty()) {
                        Toast.makeText(context, ContextCompat.getString(context, R.string.error_title_empty), Toast.LENGTH_SHORT).show()
                        return@run
                    }
                    if (description.isEmpty()) {
                        Toast.makeText(context, ContextCompat.getString(context, R.string.error_description_empty), Toast.LENGTH_SHORT).show()
                        return@run
                    }
                    showAddTaskDialog = false
                    val task = Task().apply {
                        this.title = title
                        this.description = description
                        this.concluded = false
                        this.timestamp = System.currentTimeMillis()
                    }
                    scope.launch {
                        repository.insertItem(task)
                    }
                    Toast.makeText(context, ContextCompat.getString(context, R.string.added), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    @Composable
    fun CustomTopBar(onBackClick: () -> Unit, onSearchClick: () -> Unit, onMoreClick: () -> Unit) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.my_tasks),
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                }
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                    )
                }
            },
            colors = topAppBarColors(
                containerColor = colorResource(R.color.accent),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )
    }

    @Composable
    fun EmptyScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.no_tasks))
        }
    }

    @Composable
    private fun CustomCards(tasks: List<Task>) {
        LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
            items(items = tasks) { task ->
                CustomCard(task = task)
            }
        }
    }

    @Composable
    private fun CustomCard(task: Task) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colorResource(if (task.concluded) R.color.list_item_concluded_background else R.color.list_item_background)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = colorResource(if (task.concluded) R.color.list_item_concluded_border else R.color.dialog_border)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        ) {
            CardContent(task)
        }
    }

    @Composable
    private fun CardContent(task: Task) {
        val context = LocalContext.current
        var showConfirmationDeleteDialog by rememberSaveable { mutableStateOf(false) }
        var showEditTaskDialog by rememberSaveable { mutableStateOf(false) }
        var expanded by rememberSaveable { mutableStateOf(false) }
        val dialogs = DialogsTask()
        val scope = rememberCoroutineScope()
        val repository = AppDataContainer(context).taskRepository
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // val data = result.data
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .clickable(onClick = {
                    val intent = Intent(context, DetailsActivity::class.java).apply {
                        putExtra("EXTRA_TASK", task)
                    }
                    launcher.launch(intent)
                })
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (task.concluded) Filled.Check else Filled.AccessTime, tint = if (task.concluded) colorResource(R.color.checkbox_green) else Color.White,
                    contentDescription = null
                )
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            if (expanded) {
                Text(
                    text = task.description,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = task.concluded, colors = CheckboxDefaults.colors(
                                checkedColor = colorResource(R.color.checkbox_green),
                                uncheckedColor = colorResource(R.color.accent_text),
                            ),
                            onCheckedChange = { checked ->
                                task.concluded = checked
                                scope.launch {
                                    repository.updateItem(task)
                                }
                            })
                        Text(text = stringResource(R.string.concluded))
                    }
                    Row {
                        IconButton(onClick = { showEditTaskDialog = true }) {
                            Icon(
                                imageVector = Filled.Edit,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = {
                            showConfirmationDeleteDialog = true
                        }) {
                            Icon(
                                imageVector = Filled.Delete,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
        if (showConfirmationDeleteDialog) {
            dialogs.ShowDialogConfirmation(
                title = String.format(locale = LocalLocale.current.platformLocale, stringResource(R.string.format_delete), task.title),
                onDismiss = { confirmed ->
                    showConfirmationDeleteDialog = false
                    if (confirmed) {
                        scope.launch {
                            repository.deleteItem(task)
                        }
                        Toast.makeText(context, ContextCompat.getString(context, R.string.deleted), Toast.LENGTH_SHORT).show()
                    }
                })
        }

        if (showEditTaskDialog) {
            dialogs.ShowDialogRegisterTask(initialTile = task.title, initialDescription = task.description, onCanceled = {
                showEditTaskDialog = false
            }, onConfirmed = { title, description ->
                run {
                    showEditTaskDialog = false
                    if (title.isEmpty()) {
                        Toast.makeText(context, ContextCompat.getString(context, R.string.error_title_empty), Toast.LENGTH_SHORT).show()
                        return@run
                    }
                    if (description.isEmpty()) {
                        Toast.makeText(context, ContextCompat.getString(context, R.string.error_description_empty), Toast.LENGTH_SHORT).show()
                        return@run
                    }
                    task.title = title
                    task.description = description
                    task.concluded = false
                    scope.launch {
                        repository.updateItem(task)
                    }
                    Toast.makeText(context, ContextCompat.getString(context, R.string.added), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}