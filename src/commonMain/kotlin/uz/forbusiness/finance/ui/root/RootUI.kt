package uz.forbusiness.finance.ui.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import uz.forbusiness.finance.ui.layouts.AppLayout
import uz.forbusiness.finance.ui.root.accounts.AccountsListUI
import uz.forbusiness.finance.ui.root.transactions.TransactionsListUI
import uz.forbusiness.finance.ui.theme.App4BusinessFinanceTheme

@Composable
fun RootUI(modifier: Modifier = Modifier, component: RootComponent) {
    App4BusinessFinanceTheme(useDarkTheme = false) {
        AppLayout(
            modifier = Modifier.fillMaxSize(),
            navigate = component::navigateTo
        ) {
            Children(
                stack = component.stack,
                modifier = modifier,
                animation = stackAnimation(fade()),
            ) {
                when (val child = it.instance) {
                    is RootComponent.RootChild.ListAccounts -> AccountsListUI(component = child.component)
                    is RootComponent.RootChild.TransactionsList -> TransactionsListUI(component = child.component)
                }
            }
        }
    }
}