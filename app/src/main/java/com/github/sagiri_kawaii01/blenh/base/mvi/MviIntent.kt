package com.github.sagiri_kawaii01.blenh.base.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.github.sagiri_kawaii01.blenh.util.startWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
interface MviIntent

@Composable
fun <I : MviIntent, S : MviViewState, E : MviSingleEvent>
        AbstractMviViewModel<I, S, E>.getDispatcher(startWith: I): (I) -> Unit {
    val intentChannel = remember { Channel<I>(Channel.UNLIMITED) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            intentChannel
                .consumeAsFlow()
                .startWith(startWith)
                .onEach(this@getDispatcher::processIntent)
                .collect()
        }
    }
    return remember {
        { intent: I ->
            intentChannel.trySend(intent).getOrThrow()
        }
    }
}
