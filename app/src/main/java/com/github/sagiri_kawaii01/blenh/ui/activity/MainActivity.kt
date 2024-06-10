package com.github.sagiri_kawaii01.blenh.ui.activity

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.appContext
import com.github.sagiri_kawaii01.blenh.base.AccessibilityService
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
        if (!isStartAccessibilityServiceEnable(appContext)) {
            try {
                openAccessibilityService(appContext)
            } catch (e: Exception) {
                Log.e("APP", e.toString())
            }
        }
    }

    private fun isStartAccessibilityServiceEnable(context: Context): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServices)
        while (colonSplitter.hasNext()) {
            val componentName = colonSplitter.next()
            if (componentName.equals(ComponentName(context, AccessibilityService::class.java).flattenToString(), ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    private fun openAccessibilityService(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
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