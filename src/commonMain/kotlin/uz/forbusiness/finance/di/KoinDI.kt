package uz.forbusiness.finance.di

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uz.forbusiness.finance.Database
import uz.forbusiness.finance.repository.AppRepository
import uz.forbusiness.finance.repository.AppRepositoryImpl
import uzforbusinessfinance.ACCOUNTS

expect class DatabaseDriverProvider() {
    val driver: SqlDriver
}

val driverProviderModule = module {
    singleOf(::DatabaseDriverProvider)

    single {
        Database(
            get<DatabaseDriverProvider>().driver,
            ACCOUNTSAdapter = ACCOUNTS.Adapter(
                typeAdapter = EnumColumnAdapter()
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
