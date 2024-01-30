package uz.forbusiness.finance.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.serialization.Serializable
import uz.forbusiness.finance.ui.root.accounts.AccountsListComponent
import uz.forbusiness.finance.ui.root.transactions.TransactionsListComponent

interface RootComponent {
    enum class NavigationItemDestination {
        ListAccounts,
        TransactionsList
    }

    val stack: Value<ChildStack<*, RootChild>>

    // It's possible to pop multiple screens at a time on iOS
    fun onBackClicked(toIndex: Int)

    fun navigateTo(destination: NavigationItemDestination)

    // Defines all possible child components
    sealed class RootChild {
        class ListAccounts(val component: AccountsListComponent) : RootChild()
        class TransactionsList(val component: TransactionsListComponent) : RootChild()
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.RootChild>> = childStack(
        source = navigation,
        initialConfiguration = Config.ListAccounts, // The initial child component is List
        handleBackButton = true, // Automatically pop from the stack on back button presses
        childFactory = ::child,
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.RootChild =
        when (config) {
            is Config.ListAccounts -> RootComponent.RootChild.ListAccounts(AccountsListComponent.build(componentContext))
            is Config.TransactionsList -> RootComponent.RootChild.TransactionsList(
                TransactionsListComponent.build(
                    componentContext
                )
            )
        }

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override fun navigateTo(destination: RootComponent.NavigationItemDestination) {
        when (destination) {
            RootComponent.NavigationItemDestination.ListAccounts -> {
                navigation.popTo(0)
                navigation.pushNew(Config.ListAccounts)
            }

            RootComponent.NavigationItemDestination.TransactionsList -> {
                navigation.popTo(0)
                navigation.pushNew(Config.TransactionsList)
            }
        }
    }

    @Serializable
    private sealed interface Config : Parcelable {
        data object ListAccounts : Config

        data object TransactionsList : Config
    }

}