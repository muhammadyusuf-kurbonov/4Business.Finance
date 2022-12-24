package uz.qmgroup.di

import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uz.qmgroup.Database
import uz.qmgroup.repository.AppRepository
import uz.qmgroup.repository.AppRepositoryImpl
import uz.qmgroup.viewModel.orders.OrderDialogViewModel
import uz.qmgroup.viewModel.orders.OrdersViewModel
import uz.qmgroup.viewModel.transports.TransportDialogViewModel
import uz.qmgroup.viewModel.transports.TransportsViewModel

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
    singleOf(::TransportsViewModel)
    singleOf(::TransportDialogViewModel)
    singleOf(::OrdersViewModel)
    singleOf(::OrderDialogViewModel)
}