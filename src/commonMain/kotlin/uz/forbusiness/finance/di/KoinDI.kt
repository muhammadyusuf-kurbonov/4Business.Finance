package uz.forbusiness.finance.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uz.forbusiness.finance.Database
import uz.forbusiness.finance.repository.AppRepository
import uz.forbusiness.finance.repository.AppRepositoryImpl
import uz.forbusiness.finance.viewModel.accounts.AccountDialogViewModel
import uz.forbusiness.finance.viewModel.transports.TransactionDialogViewModel
import uz.forbusiness.finance.viewModel.transports.TransactionsViewModel
import uz.forbusiness.finance.viewModel.accounts.AccountsViewModel

expect class DatabaseDriverProvider() {
    val driver: SqlDriver
}

val driverProviderModule = module {
    singleOf(::DatabaseDriverProvider)

    single {
        Database(get<DatabaseDriverProvider>().driver)
    }

    single {
        get<DatabaseDriverProvider>().driver
    }
}

val repositoryModule = module {
    singleOf(::AppRepositoryImpl) bind AppRepository::class
}

val viewModelsModule = module {
    singleOf(::TransactionsViewModel)
    singleOf(::AccountsViewModel)
    singleOf(::TransactionDialogViewModel)
    singleOf(::AccountDialogViewModel)
}