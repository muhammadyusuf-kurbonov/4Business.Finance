package uz.qmgroup.viewModel.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren

open class BaseViewModel {
    private val context = Job()
    protected val viewModelScope: CoroutineScope = CoroutineScope(context)

    open fun clear() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}