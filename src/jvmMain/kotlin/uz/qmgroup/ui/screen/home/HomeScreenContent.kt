package uz.qmgroup.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreenContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text("Home Screen", style = MaterialTheme.typography.h5)
    }
}