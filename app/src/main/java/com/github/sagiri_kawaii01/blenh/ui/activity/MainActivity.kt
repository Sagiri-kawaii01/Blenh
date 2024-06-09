package com.github.sagiri_kawaii01.blenh.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardIntent
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardListState
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardScreen
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardViewModel
import com.github.sagiri_kawaii01.blenh.ui.theme.BlenhTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlenhTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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