package uz.forbusiness.finance.di

import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

object AppKoinComponent : KoinComponent {
    init {
        startKoin {
            printLogger()

            modules(driverProviderModule, repositoryModule)
        }
    }
}