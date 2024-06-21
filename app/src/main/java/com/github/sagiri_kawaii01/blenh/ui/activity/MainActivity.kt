package com.github.sagiri_kawaii01.blenh.ui.activity

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.sagiri_kawaii01.blenh.appContext
import com.github.sagiri_kawaii01.blenh.base.AccessibilityService
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.TimePeriodType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.ui.local.LocalNavController
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_BILL_LIST
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_DASHBOARD
import com.github.sagiri_kawaii01.blenh.ui.screen.billlist.BillListScreen
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardIntent
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardListState
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardScreen
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardViewModel
import com.github.sagiri_kawaii01.blenh.ui.theme.BlenhTheme
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val tabItem = listOf(
    "收支统计" to ROUTE_DASHBOARD,
    "账单列表" to ROUTE_BILL_LIST
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
        }
        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                BlenhTheme {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()

                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet(
                                modifier = Modifier.width(240.dp)
                            ) {
                                Column {
                                    tabItem.forEach { item ->
                                        NavigationDrawerItem(
                                            label = {
                                                Text(text = item.first)
                                            },
                                            selected = navBackStackEntry?.destination?.route == item.second,
                                            onClick = {
                                                scope.launch {
                                                    drawerState.close()
                                                    navController.navigate(item.second)
                                                }
                                            })
                                    }
                                }
                            }
                        },
                        drawerState = drawerState,
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        Box(modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center) {
                                            val text = when (navBackStackEntry?.destination?.route) {
                                                ROUTE_DASHBOARD -> "收支统计"
                                                ROUTE_BILL_LIST -> "账单列表"
                                                else -> navBackStackEntry?.destination?.route ?: "未知页面"
                                            }
                                            Text(text = text, style = Typography.titleMedium)
                                        }
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                if (drawerState.isClosed) {
                                                    drawerState.open()
                                                } else {
                                                    drawerState.close()
                                                }
                                            }
                                        }) {
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
                            modifier = Modifier.fillMaxSize(),
                        ) { innerPadding ->
                            Surface(
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                NavHost(navController = navController, startDestination = ROUTE_DASHBOARD) {
                                    composable(ROUTE_DASHBOARD) { DashboardScreen() }
                                    composable(ROUTE_BILL_LIST) { BillListScreen() }
                                }
                            }
                        }
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
        val enabledServices = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
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