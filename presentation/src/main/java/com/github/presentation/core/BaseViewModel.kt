package com.github.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface ViewEvent

interface ViewState

interface ViewSideEffect

abstract class BaseViewModel<Event : ViewEvent, State : ViewState, Effect : ViewSideEffect> :
    ViewModel() {

    abstract val initialState: State

    val viewState: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }

    private val viewEvent: MutableSharedFlow<Event> = MutableSharedFlow()

    val viewEffect: MutableSharedFlow<Effect> by lazy { MutableSharedFlow() }

    init {
        collectViewEvents()
    }

    fun sendViewEvent(event: Event) {
        viewModelScope.launch {
            viewEvent.emit(event)
        }
    }

    fun setViewState(reducer: State.() -> State) {
        val newState = viewState.value.reducer()
        println("STATE: $newState")
        viewState.value = newState
    }

    fun setViewEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch {
            viewEffect.emit(effectValue)
        }
    }

    private fun collectViewEvents() {
        viewModelScope.launch {
            viewEvent.collect {
                handleViewEvents(it)
            }
        }
    }

    abstract fun handleViewEvents(viewEvent: Event)
}
