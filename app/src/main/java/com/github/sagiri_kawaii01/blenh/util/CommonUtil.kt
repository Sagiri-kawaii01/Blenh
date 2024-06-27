package com.github.sagiri_kawaii01.blenh.util


import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.pm.PackageInfoCompat
import com.github.sagiri_kawaii01.blenh.R
import com.github.sagiri_kawaii01.blenh.appContext

object CommonUtil {
    private var uiThreadHandler: Handler = Handler(Looper.getMainLooper())

    fun openBrowser(url: String) {
        try {
            val uri: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            "Can't find the browser! Link: $url".showToast(Toast.LENGTH_LONG)
        }
    }

    private fun CharSequence.showToast(duration: Int = Toast.LENGTH_SHORT) {
        uiThreadHandler.post {
            val toast = Toast.makeText(appContext, this, duration)
            toast.duration = duration
            toast.show()
        }
    }


    fun getAppVersionName(): String {
        var appVersionName = ""
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                appContext.applicationContext
                    .packageManager
                    .getPackageInfo(appContext.packageName, PackageManager.PackageInfoFlags.of(0L))
            } else {
                appContext.applicationContext
                    .packageManager
                    .getPackageInfo(appContext.packageName, 0)
            }
            appVersionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appVersionName
    }

    fun getAppVersionCode(): Long {
        var appVersionCode: Long = 0
        try {
            val packageInfo = appContext.applicationContext
                .packageManager
                .getPackageInfo(appContext.packageName, 0)
            appVersionCode = PackageInfoCompat.getLongVersionCode(packageInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appVersionCode
    }

    fun getAppInfo(pm: PackageManager, packageName: String): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags
                    .of(PackageManager.MATCH_UNINSTALLED_PACKAGES.toLong())
            )
        } else {
            pm.getPackageInfo(packageName, PackageManager.MATCH_UNINSTALLED_PACKAGES)
        }
    }
}