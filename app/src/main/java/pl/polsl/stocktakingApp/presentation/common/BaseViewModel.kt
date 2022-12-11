package pl.polsl.stocktakingApp.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<out STATE>(protected val defaultDispatcher: CoroutineDispatcher) :
    ViewModel() {
    protected val jobs = mutableListOf<Job>()
    abstract val initialState: STATE
    protected abstract val _state: Flow<STATE>
    val state: StateFlow<STATE> by lazy {
        _state.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            initialState
        )
    }

    protected val _events = MutableSharedFlow<Event>()
    val events: Flow<Event> = _events

    protected fun launch(
        dispatcher: CoroutineDispatcher = defaultDispatcher,
        action: suspend CoroutineScope.() -> Unit
    ) {
        jobs += viewModelScope.launch(dispatcher) {
            action()
        }
    }

    protected suspend fun suspendLaunch(
        dispatcher: CoroutineDispatcher = defaultDispatcher,
        action: suspend CoroutineScope.() -> Unit
    ) {
        val job = viewModelScope.launch(dispatcher) { action() }
        jobs += job
        job.join()
    }

    protected operator fun MutableList<Job>.plusAssign(job: Job) {
        this.add(job)
    }

    override fun onCleared() {
        jobs.forEach { it.cancel() }
        jobs.clear()
        super.onCleared()
    }
}