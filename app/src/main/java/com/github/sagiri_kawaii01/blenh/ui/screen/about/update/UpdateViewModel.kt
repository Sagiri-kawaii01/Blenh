package com.github.sagiri_kawaii01.blenh.ui.screen.about.update

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import com.github.sagiri_kawaii01.blenh.appContext
import com.github.sagiri_kawaii01.blenh.base.mvi.AbstractMviViewModel
import com.github.sagiri_kawaii01.blenh.model.repository.UpdateRepository
import com.github.sagiri_kawaii01.blenh.util.CommonUtil.getAppVersionCode
import com.github.sagiri_kawaii01.blenh.util.CommonUtil.openBrowser
import com.github.sagiri_kawaii01.blenh.util.catchMap
import com.github.sagiri_kawaii01.blenh.util.flowOnIo
import com.github.sagiri_kawaii01.blenh.util.startWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.coroutineContext
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@HiltViewModel
class UpdateViewModel @Inject constructor(private var updateRepo: UpdateRepository) :
    AbstractMviViewModel<UpdateIntent, UpdateState, UpdateEvent>() {

    override val viewState: StateFlow<UpdateState>

    init {
        val initialVS = UpdateState.initial()

        viewState = merge(
            intentSharedFlow.filter { it is UpdateIntent.CheckUpdate && !it.isRetry }.take(1),
            intentSharedFlow.filter { it is UpdateIntent.CheckUpdate && it.isRetry },
            intentSharedFlow.filterNot { it is UpdateIntent.CheckUpdate }
        )
            .shareWhileSubscribed()
            .toUpdatePartialStateChangeFlow()
            .debugLog("UpdatePartialStateChange")
            .sendSingleEvent()
            .scan(initialVS) {
                vs, change -> change.reduce(vs)
            }
            .debugLog("ViewState")
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                initialVS
            )
    }

    private fun Flow<UpdatePartialStateChange>.sendSingleEvent(): Flow<UpdatePartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                is UpdatePartialStateChange.Error -> UpdateEvent.CheckError(change.msg)
                is UpdatePartialStateChange.CheckUpdate.NoUpdate,
                is UpdatePartialStateChange.CheckUpdate.HasUpdate -> UpdateEvent.CheckSuccess()

                else -> return@onEach
            }
            sendEvent(event)
        }
    }

    private fun String.toLongOrDefault(defaultValue: Long): Long {
        return try {
            toLong()
        } catch (_: NumberFormatException) {
            defaultValue
        }
    }

    private fun Date.toDateTimeString(
        dateStyle: Int = SimpleDateFormat.MEDIUM,
        timeStyle: Int = SimpleDateFormat.MEDIUM,
        locale: Locale = Locale.getDefault()
    ): String = SimpleDateFormat
        .getDateTimeInstance(dateStyle, timeStyle, locale)
        .format(this)

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun SharedFlow<UpdateIntent>.toUpdatePartialStateChangeFlow(): Flow<UpdatePartialStateChange> {
        return merge(
            filterIsInstance<UpdateIntent.CheckUpdate>().flatMapConcat {
                merge(
                    flowOf(UpdatePartialStateChange.LoadingDialog),
                    updateRepo.checkUpdate().map { data ->
                        if (getAppVersionCode() < data.tagName.toLongOrDefault(2L)) {
                            val date = SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                                Locale.getDefault()
                            ).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }.parse(data.publishedAt)
                            val publishedAt: String = date?.toDateTimeString() ?: data.publishedAt

                            UpdatePartialStateChange.CheckUpdate.HasUpdate(
                                data.copy(publishedAt = publishedAt)
                            )
                        } else {
                            UpdatePartialStateChange.CheckUpdate.NoUpdate
                        }
                    }
                ).catchMap { UpdatePartialStateChange.Error(it.message.orEmpty()) }
            },

            filterIsInstance<UpdateIntent.Update>().flatMapLatest { intent ->
                openBrowser(intent.url ?: "https://github.com/Sagiri-kawaii01/blenh")
                flowOf(UpdatePartialStateChange.RequestUpdate)
            },
        )
    }

    private fun downloadApk(
        context: Context,
        url: String,
        onComplete: (file: File) -> Unit,
        onError: (t: Throwable) -> Unit,
        onProgress: (progress: Int) -> Unit,
    ) {
        val call = updateRepo.downloadApk(url)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "latest.apk")
                        var inputStream: InputStream? = null
                        var outputStream: FileOutputStream? = null

                        try {
                            val fileSize = body.contentLength()
                            var fileSizeDownloaded: Long = 0
                            inputStream = body.byteStream()
                            outputStream = FileOutputStream(file)

                            val data = ByteArray(4096)
                            var count: Int

                            var p = 0
                            while (inputStream.read(data).also { count = it } != -1) {
                                outputStream.write(data, 0, count)
                                fileSizeDownloaded += count
                                val p2 = (fileSizeDownloaded * 100 / fileSize).toInt()
                                if (p2 > p) {
                                    onProgress(p2)
                                    p = p2
                                }
                            }

                            outputStream.flush()
                            onComplete(file)
                        } catch (e: IOException) {
                            onError(e)
                        } finally {
                            inputStream?.close()
                            outputStream?.close()
                        }
                    } ?: onError(NullPointerException("Response body is null"))
                } else {
                    onError(HttpException(response))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onError(t)
            }
        })
    }

    private fun installApk(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }
}