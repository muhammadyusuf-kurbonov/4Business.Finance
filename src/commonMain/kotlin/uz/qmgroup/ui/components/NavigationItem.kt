package uz.qmgroup.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun NavigationItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    CompositionLocalProvider(
        LocalContentAlpha provides if (selected) ContentAlpha.high else ContentAlpha.disabled,
        LocalContentColor provides if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
    ) {
        Row(
            modifier = modifier.defaultMinSize(minWidth = 256.dp)
                .clip(RoundedCornerShape(4.dp))
                .composed {
                    if (selected) {
                        this.background(MaterialTheme.colors.onSurface.copy(alpha = 0.05f))
                    } else {
                        this
                    }
                }
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = label,
            )

            Text(label, style = MaterialTheme.typography.h6)
        }
    }
}

@Preview
@Composable
fun PreviewNavigationItem() {
    MaterialTheme {
        Column {
            NavigationItem(
                icon = Icons.Outlined.LocalShipping,
                label = "Shippings",
                selected = false,
                onClick = {}
            )

            NavigationItem(
                icon = Icons.Outlined.LocalShipping,
                label = "Shippings",
                selected = true,
                onClick = {}
            )
        }
    }
}