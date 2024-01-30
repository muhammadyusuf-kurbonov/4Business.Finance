package uz.forbusiness.finance.ui.root.transactions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStart
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.koin.core.component.inject
import uz.forbusiness.finance.di.AppKoinComponent
import uz.forbusiness.finance.models.Transaction
import uz.forbusiness.finance.repository.AppRepository
import uz.forbusiness.finance.ui.root.transactions.dialogs.add.AddTransactionUIState

interface TransactionsListComponent {
    companion object {
        fun build(context: ComponentContext): TransactionsListComponent = DefaultTransactionsListComponent(context)
    }

    val model: Value<TransactionsListUIState>
    val addTransactionUIState: Value<AddTransactionUIState>

    fun openAddDialog()
    fun closeAddDialog()

    fun saveTransaction(transaction: Transaction)

    private class DefaultTransactionsListComponent(
        val context: ComponentContext
    ) : TransactionsListComponent, ComponentContext by context {
        override val model: MutableValue<TransactionsListUIState> = MutableValue(TransactionsListUIState.Loading)
        override val addTransactionUIState: MutableValue<AddTransactionUIState> =
            MutableValue(AddTransactionUIState.None)

        override fun openAddDialog() {
            addTransactionUIState.update { AddTransactionUIState.LoadingAccounts }

            scope.launch {
                val accounts = repository.getAllAccounts().first()
                addTransactionUIState.update { AddTransactionUIState.WaitingForInput(accounts) }
            }
        }

        override fun closeAddDialog() {
            addTransactionUIState.update { AddTransactionUIState.None }
        }

        override fun saveTransaction(transaction: Transaction) {
            addTransactionUIState.update { AddTransactionUIState.Saving }
            scope.launch {
                repository.insertNewTransaction(
                    transaction.note,
                    transaction.amount,
                    transaction.dateTime,
                    transaction.debit,
                    transaction.credit
                )

                addTransactionUIState.update { AddTransactionUIState.SaveCompleted }
            }
        }

        private val repository by AppKoinComponent.inject<AppRepository>()

        private val scope = CoroutineScope(Dispatchers.Default + Job())

        init {
            lifecycle.doOnStart {
                model.update { TransactionsListUIState.Loading }

                scope.launch {
                    repository.getAllTransactions().collect { transactions ->
                        if (transactions.isNotEmpty()) {
                            model.update { TransactionsListUIState.DataFetched(transactions) }
                        } else {
                            model.update { TransactionsListUIState.NoData }
                        }
                    }
                }
            }

            lifecycle.doOnDestroy {
                scope.coroutineContext.job.cancel()
            }
        }
    }
}
