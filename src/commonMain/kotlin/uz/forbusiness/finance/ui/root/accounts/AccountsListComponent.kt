package uz.forbusiness.finance.ui.root.accounts

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStart
import kotlinx.coroutines.*
import org.koin.core.component.inject
import uz.forbusiness.finance.di.AppKoinComponent
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.repository.AppRepository
import uz.forbusiness.finance.ui.root.accounts.dialogs.add.AddAccountUIState

interface AccountsListComponent {
    companion object {
        fun build(context: ComponentContext): AccountsListComponent = DefaultAccountsListComponent(context)
    }

    val model: Value<AccountsListUIState>
    val addDialogState: Value<AddAccountUIState>

    fun startAddDialog()
    fun closeAddDialog()

    fun saveNewAccount(account: Account)
    private class DefaultAccountsListComponent(
        val context: ComponentContext
    ) : AccountsListComponent, ComponentContext by context {
        override val model: MutableValue<AccountsListUIState> = MutableValue(AccountsListUIState.Loading)
        override val addDialogState: MutableValue<AddAccountUIState> = MutableValue(AddAccountUIState.None)

        private val repository by AppKoinComponent.inject<AppRepository>()

        private val scope = CoroutineScope(Dispatchers.Default + Job())

        init {
            lifecycle.doOnStart {
                model.update { AccountsListUIState.Loading }

                scope.launch {
                    repository.getAllAccountsWithBalances().collect { accounts ->
                        println("Fetched $accounts")
                        if (accounts.isNotEmpty()) {
                            model.update { AccountsListUIState.DataFetched(accounts) }
                        } else {
                            model.update { AccountsListUIState.NoData }
                        }
                    }
                }
            }

            lifecycle.doOnDestroy {
                scope.coroutineContext.job.cancel()
            }
        }

        override fun startAddDialog() {
            addDialogState.update { AddAccountUIState.WaitingForInput }
        }

        override fun closeAddDialog() {
            addDialogState.update { AddAccountUIState.None }
        }

        override fun saveNewAccount(account: Account) {
            addDialogState.update { AddAccountUIState.Saving }

            scope.launch {
                repository.saveNewAccount(account)
                addDialogState.update { AddAccountUIState.SaveCompleted }
            }
        }
    }
}
