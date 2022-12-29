package uz.qmgroup.di

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.logs.LogSqliteDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uz.qmgroup.Database
import uz.qmgroup.repository.AppRepository
import uz.qmgroup.repository.AppRepositoryImpl
import uz.qmgroup.viewModel.shipments.ShipmentAddEditViewModel
import uz.qmgroup.viewModel.shipments.ShipmentsViewModel
import uz.qmgroup.viewModel.transports.TransportDialogViewModel
import uz.qmgroup.viewModel.transports.TransportsSearchViewModel
import uz.qmgroup.viewModel.transports.TransportsViewModel

expect class DatabaseDriverProvider() {
    val driver: SqlDriver
}

val driverProviderModule = module {
    singleOf(::DatabaseDriverProvider)

    single {
        Database(
            LogSqliteDriver(
                sqlDriver = get<DatabaseDriverProvider>().driver,
                logger = {
                    println("Running query $it")
                }
            )
        )
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
    singleOf(::ShipmentsViewModel)
    singleOf(::ShipmentAddEditViewModel)
    singleOf(::TransportsSearchViewModel)
}