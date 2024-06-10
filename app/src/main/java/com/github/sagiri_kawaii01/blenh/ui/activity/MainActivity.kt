package com.github.sagiri_kawaii01.blenh.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardIntent
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardListState
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardScreen
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardViewModel
import com.github.sagiri_kawaii01.blenh.ui.theme.BlenhTheme
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlenhTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Box(modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center) {
                                    Text(text = "收支统计", style = Typography.titleMedium)
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        Icons.Filled.Menu, 
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            actions = {
                                Spacer(modifier = Modifier.width(48.dp))
                            },
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        DashboardScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String,
             modifier: Modifier = Modifier,
             viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.viewState.collectAsState()

    viewModel.getDispatcher(startWith = DashboardIntent.GetBillList(TimePeriodType.Week))

    Text(
        text = "Hello world!",
        modifier = modifier
    )

    when (uiState.dashboardListState) {
        is DashboardListState.Success -> {
            LazyColumn {
                items((uiState.dashboardListState as DashboardListState.Success).billList) {
                    Text(text = it.time)
                }
            }
        }
        else -> Unit
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BlenhTheme {
        Greeting("Android")
    }
}