package com.github.sagiri_kawaii01.blenh.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.sagiri_kawaii01.blenh.BuildConfig
import com.github.sagiri_kawaii01.blenh.appContext
import com.github.sagiri_kawaii01.blenh.base.AccessibilityService
import com.github.sagiri_kawaii01.blenh.ui.component.PermissionLine
import com.github.sagiri_kawaii01.blenh.ui.component.WaitingDialog
import com.github.sagiri_kawaii01.blenh.ui.local.LocalNavController
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_ADD_BILL
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_BILL_LIST
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_DASHBOARD
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_EDIT_BILL
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_PAY_DETAIL
import com.github.sagiri_kawaii01.blenh.ui.screen.about.update.UpdateDialog
import com.github.sagiri_kawaii01.blenh.ui.screen.edit.EditScreen
import com.github.sagiri_kawaii01.blenh.ui.screen.billlist.BillListScreen
import com.github.sagiri_kawaii01.blenh.ui.screen.dashboard.DashboardScreen
import com.github.sagiri_kawaii01.blenh.ui.screen.detail.DetailScreen
import com.github.sagiri_kawaii01.blenh.ui.theme.BlenhTheme
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography
import com.github.sagiri_kawaii01.blenh.util.CommonUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

val tabItem = listOf(
    "收支统计" to ROUTE_DASHBOARD,
    "账单列表" to ROUTE_BILL_LIST,
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private var showPermissionDialog by mutableStateOf(false)
    private var showUpdateDialog by mutableStateOf(true)
    private var accessibilityEnable by mutableStateOf(false)
    private var overlayEnable by mutableStateOf(false)

    private fun removeApk() {
        val directory = getExternalFilesDir(null)
        if (directory != null && directory.isDirectory) {
            val apkFiles = directory.listFiles { file -> file.extension == "apk" }
            apkFiles?.forEach { file ->
                if (file.exists()) {
                    file.delete()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        removeApk()
        if (BuildConfig.ENABLE_ACCESSIBILITY) {
            if (isStartAccessibilityServiceEnable(appContext)) {
                accessibilityEnable = true
            }
            if (Settings.canDrawOverlays(this)) {
                overlayEnable = true
            }
            showPermissionDialog = !accessibilityEnable || !overlayEnable
        }
        enableEdgeToEdge()
        setContent {
            WaitingDialog(
                visible = showPermissionDialog,
                title = "请开启以下权限",
            ) {
                Column {
                    PermissionLine(
                        text = "无障碍",
                        enable = accessibilityEnable,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        try {
                            openAccessibilityService(appContext)
                        } catch (e: Exception) {
                            Log.e("Accessibility", e.toString())
                        }
                    }
                    PermissionLine(
                        text = "显示在应用上层",
                        enable = overlayEnable,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                        startActivity(intent)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "此外，请确保开启自启动权限，及无限制电池优化策略")
                }
            }

            if (!showPermissionDialog && showUpdateDialog) {
                UpdateDialog {
                    showUpdateDialog = false
                }
            }

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
                                            val text = when (navBackStackEntry?.destination?.route?.substringBefore("?")) {
                                                ROUTE_DASHBOARD -> "收支统计"
                                                ROUTE_BILL_LIST -> "账单列表"
                                                ROUTE_PAY_DETAIL -> "支付详情"
                                                ROUTE_ADD_BILL -> "添加账单"
                                                ROUTE_EDIT_BILL -> "编辑账单"
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
                                    composable(ROUTE_BILL_LIST) {
                                        BillListScreen()
                                    }
                                    composable(
                                        route = "$ROUTE_PAY_DETAIL?id={id}",
                                        arguments = listOf(navArgument("id") { defaultValue = -1 })
                                    ) { backStackEntry  ->
                                        DetailScreen(billId = backStackEntry.arguments!!.getInt("id"))
                                    }
                                    composable(ROUTE_ADD_BILL) {
                                        EditScreen()
                                    }
                                    composable(
                                        route = "$ROUTE_EDIT_BILL?id={id}",
                                        arguments = listOf(navArgument("id") { defaultValue = -1} )
                                    ) { backStackEntry ->
                                        EditScreen(billId = backStackEntry.arguments!!.getInt("id"))
                                    }
                                }
                            }
                        }
                    }
                }
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

    override fun onResume() {
        super.onResume()
        if (BuildConfig.ENABLE_ACCESSIBILITY) {
            if (isStartAccessibilityServiceEnable(appContext)) {
                accessibilityEnable = true
            }
            if (Settings.canDrawOverlays(this)) {
                overlayEnable = true
            }
            showPermissionDialog = !accessibilityEnable || !overlayEnable
        }
    }

    private fun openAccessibilityService(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}