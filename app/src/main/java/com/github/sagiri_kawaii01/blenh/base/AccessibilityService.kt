package com.github.sagiri_kawaii01.blenh.base

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.github.sagiri_kawaii01.blenh.di.DatabaseModule
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.db.AppDatabase
import com.github.sagiri_kawaii01.blenh.model.db.repository.BillRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.CategoryRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.IconRepository
import com.github.sagiri_kawaii01.blenh.model.db.repository.TypeRepository
import com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet.BottomSheetContent
import com.github.sagiri_kawaii01.blenh.ui.screen.bottomsheet.BottomSheetViewModel
import com.github.sagiri_kawaii01.blenh.ui.theme.BlenhTheme
import com.github.sagiri_kawaii01.blenh.util.access.WechatAccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AccessibilityService: AccessibilityService(), SavedStateRegistryOwner, ViewModelStoreOwner {

    private var t = 0L
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var rt = System.currentTimeMillis()
    private lateinit var windowManager: WindowManager
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController by lazy {
        SavedStateRegistryController.create(this)
    }

    private lateinit var overlayView: View
    private val appDatabase: AppDatabase by lazy {
        DatabaseModule.provideAppDatabase(this)
    }

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry
    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
    override val viewModelStore = ViewModelStore()

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventTime - t < 250) {
            return
        }
        if (event.source == null) {
            return
        }
        t = event.eventTime
        Log.d("AccessibilityService", event.toString())
        val bill = when (event.packageName) {
            "com.tencent.mm" -> WechatAccess.handle(event.source!!)
            else -> null
        }
        if (null != bill) {
            Log.d("Bill", bill.money.toString())
            Log.d("Bill", bill.target!!)
            Log.d("Bill", bill.payMethod!!)
            try {
                showBottomSheet(bill)
            } catch (e: Exception) {
                Log.e("AccessibilityService", e.toString())
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("ClickableViewAccessibility")
    private fun showBottomSheet(bill: BillBean) {
        val nrt = System.currentTimeMillis()
        if (nrt - rt < 500) {
            return
        }
        rt = nrt
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        overlayView = ComposeView(this).apply {
            setContent {
                BlenhTheme {
                    CompositionLocalProvider(
                        LocalViewModelStoreOwner provides this@AccessibilityService
                    ) {

                        BottomSheetContent(
                            bill = bill,
                            onDismissRequest = {
                                serviceScope.launch {
                                    removeView()
                                }
                            }
                        ) {
                            BottomSheetViewModel(
                                BillRepository(DatabaseModule.provideBillDao(appDatabase)),
                                TypeRepository(DatabaseModule.provideTypeDao(appDatabase)),
                                IconRepository(DatabaseModule.provideIconDao(appDatabase)),
                                CategoryRepository(DatabaseModule.provideCategoryDao(appDatabase))
                            )
                        }
                    }
                }
            }
        }

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )

        layoutParams.gravity = Gravity.BOTTOM
        overlayView.setViewTreeLifecycleOwner(this@AccessibilityService)
        overlayView.setViewTreeSavedStateRegistryOwner(this@AccessibilityService)

        Log.d("AccessibilityService", "showBottomSheet")
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        windowManager.addView(overlayView, layoutParams)
        overlayView.requestLayout()
        overlayView.invalidate()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private suspend fun removeView() {
        withContext(Dispatchers.Main) {
            viewModelStore.clear()
            Log.d("AccessibilityService", "RemoveShowBottomSheet")
            if (::overlayView.isInitialized) {
                windowManager.removeView(overlayView)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.launch {
            runBlocking {
                removeView()
            }
        }
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        viewModelStore.clear()
    }

    override fun onInterrupt() {
        Log.d("AccessibilityService", "onInterrupt")
    }
}