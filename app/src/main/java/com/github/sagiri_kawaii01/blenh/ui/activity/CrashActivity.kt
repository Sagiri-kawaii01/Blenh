package com.github.sagiri_kawaii01.blenh.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.github.sagiri_kawaii01.blenh.R
import com.github.sagiri_kawaii01.blenh.ui.theme.BlenhTheme
import com.github.sagiri_kawaii01.blenh.util.CommonUtil.getAppVersionCode
import com.github.sagiri_kawaii01.blenh.util.CommonUtil.getAppVersionName

/**
 * CrashActivity
 */
class CrashActivity : ComponentActivity() {
    companion object {
        const val CRASH_INFO = "crashInfo"

        fun start(context: Context, crashInfo: String) {
            val intent = Intent(context, CrashActivity::class.java)
            intent.putExtra(CRASH_INFO, crashInfo)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val crashInfo = intent.getStringExtra(CRASH_INFO)
        val message = buildString {
            append("VersionName: ").append(getAppVersionName()).append("\n")
            append("VersionCode: ").append(getAppVersionCode()).append("\n")
            append("Brand: ").append(Build.BRAND).append("\n")
            append("Model: ").append(Build.MODEL).append("\n")
            append("SDK Version: ").append(Build.VERSION.SDK_INT).append("\n")
            append("ABI: ").append(Build.SUPPORTED_ABIS.firstOrNull().orEmpty()).append("\n\n")
            append("Crash Info: \n")
            append(crashInfo)
        }

        setContent {
            BlenhTheme {
                CrashScreen(
                    message = message
                )
            }
        }
    }
}

@Composable
private fun CrashScreen(
    message: String
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Default.BugReport,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.crashed),
                style = MaterialTheme.typography.headlineLarge,
            )

            Spacer(modifier = Modifier.height(30.dp))

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.crash_screen_crash_log),
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(10.dp))
            SelectionContainer {
                Text(text = message, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
