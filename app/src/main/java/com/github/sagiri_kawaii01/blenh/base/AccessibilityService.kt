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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
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
import kotlinx.coroutines.SupervisorJob

class AccessibilityService: AccessibilityService(), SavedStateRegistryOwner, ViewModelStoreOwner {

    private var t = 0L
    private var rt = System.currentTimeMillis()
    private lateinit var windowManager: WindowManager
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController by lazy {
        SavedStateRegistryController.create(this)
    }

    private lateinit var overlayView: View
    private lateinit var touchInterceptorView: View
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

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        touchInterceptorView = View(this).apply {
            setBackgroundColor(0x00000000) // 全透明
            setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    removeView()
                    true
                } else {
                    false
                }
            }
        }

        val interceptorParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            screenHeight / 3 * 2,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        interceptorParams.gravity = Gravity.TOP

        // 添加触摸拦截层到窗口
        windowManager.addView(touchInterceptorView, interceptorParams)

        overlayView = ComposeView(this).apply {
            setContent {
                BlenhTheme {
                    CompositionLocalProvider(
                        LocalViewModelStoreOwner provides this@AccessibilityService
                    ) {
                        BottomSheetContent(bill = bill) {
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

        overlayView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                removeView()
                true
            } else {
                false
            }
        }
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            screenHeight / 3,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        layoutParams.gravity = Gravity.BOTTOM
        overlayView.setViewTreeLifecycleOwner(this@AccessibilityService)
        overlayView.setViewTreeSavedStateRegistryOwner(this@AccessibilityService)

        Log.d("AccessibilityService", "showBottomSheet")
        windowManager.addView(overlayView, layoutParams)
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun removeView() {
        viewModelStore.clear()
        Log.d("AccessibilityService", "RemoveShowBottomSheet")
        if (::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
        if (::touchInterceptorView.isInitialized) {
            windowManager.removeView(touchInterceptorView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeView()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        viewModelStore.clear()
    }

    override fun onInterrupt() {
        Log.d("AccessibilityService", "onInterrupt")
    }
}