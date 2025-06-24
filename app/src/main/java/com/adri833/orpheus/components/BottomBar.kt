package com.adri833.orpheus.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.R
import com.adri833.orpheus.ui.pulseScale

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val bottomNavItems = BottomNavItemsProvider.getBottomNavItems()

    var pressedRoute by remember { mutableStateOf<String?>(null) }

    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            val isPressed = pressedRoute == item.route
            val scale = pulseScale(isPressed) {
                pressedRoute = null
            }

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                        }
                    }
                    pressedRoute = item.route
                },
                icon = {
                    Icon(
                        painter = if (selected) item.filledIcon else item.outlinedIcon,
                        tint = if (selected) Color.White else Color.Gray,
                        contentDescription = stringResource(item.label),
                        modifier = Modifier.size((24 * scale).dp)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.label),
                        fontFamily = FontFamily(Font(R.font.merriweather_bold)),
                        fontSize = 10.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}