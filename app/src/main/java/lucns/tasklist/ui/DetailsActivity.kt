package lucns.tasklist.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.codelab.basics.ui.theme.BasicsAppTheme
import lucns.tasklist.R
import lucns.tasklist.models.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backgroundColor = ContextCompat.getColor(this, R.color.app_background)
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.dark(backgroundColor))

        val task = intent.getParcelableExtra("EXTRA_TASK", Task::class.java)
        setContent {
            val backgroundColor = colorResource(R.color.app_background).toArgb()
            SideEffect {
                enableEdgeToEdge(navigationBarStyle = SystemBarStyle.dark(backgroundColor))
            }
            BasicsAppTheme {
                DetailsScreen(task!!)
            }
        }
    }

    @Composable
    fun DetailsScreen(task: Task) {
        val context = LocalContext.current
        Scaffold(
            topBar = {
                CustomTopBar(onBackClick = {
                    (context as? Activity)?.finish()
                }, onMoreClick = {
                    Toast.makeText(context, ContextCompat.getString(context, R.string.not_implemented), Toast.LENGTH_SHORT).show()
                })
            }, content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    CustomCard(task)
                }
            }
        )
    }

    @Composable
    fun CustomTopBar(onBackClick: () -> Unit, onMoreClick: () -> Unit) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.task_details),
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
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
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
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            CardContent(task)
        }
    }

    @Composable
    private fun CardContent(task: Task) {
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
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (task.concluded) Filled.Check else Filled.AccessTime, tint = if (task.concluded) colorResource(R.color.text_green) else Color.White,
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
            }

            Column(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp).fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.description), color = colorResource(if (task.concluded) R.color.text_green else R.color.accent_text),
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
                Text(
                    text = task.description,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 20.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(if (task.concluded) R.string.concluded_2 else R.string.not_concluded), color = Color.White.copy(alpha = 0.75f))
                Text(text = convertLongToTime(task.timestamp), color = Color.White.copy(alpha = 0.75f))
            }
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}