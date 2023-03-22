package uz.forbusiness.finance.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import uz.forbusiness.finance.ui.screen.app.AppViewModel
import uz.forbusiness.finance.viewModel.transports.TransactionDialogViewModel
import uz.forbusiness.finance.viewModel.transports.TransactionsViewModel

object AppKoinComponent : KoinComponent {
    init {
        val localViewModelsModule = module {
            singleOf(::AppViewModel)
        }

        startKoin {
            printLogger()

            modules(driverProviderModule, repositoryModule, viewModelsModule, localViewModelsModule)
        }
    }

    val appViewModel: AppViewModel by inject()
    val transactionsViewModel: TransactionsViewModel by inject()
    val transactionDialogViewModel: TransactionDialogViewModel by inject()
}