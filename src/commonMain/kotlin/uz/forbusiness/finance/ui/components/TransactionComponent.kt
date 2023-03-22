package uz.forbusiness.finance.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.forbusiness.finance.models.Transaction
import java.text.NumberFormat

@Composable
fun TransactionComponent(modifier: Modifier = Modifier, transaction: Transaction) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (transaction.amount > 0) Icons.Outlined.ArrowUpward else Icons.Outlined.ArrowDownward,
            contentDescription = null
        )

        Column {
            Text(transaction.note, style = MaterialTheme.typography.body1)
            Text(NumberFormat.getCurrencyInstance().format(transaction.amount), style = MaterialTheme.typography.body2)
        }
    }
}