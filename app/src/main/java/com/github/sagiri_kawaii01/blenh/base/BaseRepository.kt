package com.github.sagiri_kawaii01.blenh.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


open class BaseRepository {
    protected fun <T> flowOnIo(block: suspend FlowCollector<T>.() -> Unit) =
        flow(block).flowOn(Dispatchers.IO)
}