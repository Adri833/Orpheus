package com.adri833.orpheus.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.R
import kotlinx.coroutines.launch

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val bottomNavItems = BottomNavItemsProvider.getBottomNavItems()
    val coroutineScope = rememberCoroutineScope()

    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route

            val scale = remember { Animatable(1f) }

            NavigationBarItem(
                selected = selected,
                onClick = {
                    coroutineScope.launch {
                        if (!selected) {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                        }
                        scale.animateTo(0.8f, animationSpec = tween(150))
                        scale.animateTo(1f, animationSpec = tween(150))
                    }
                },
                icon = {
                    Icon(
                        painter = if (selected) item.filledIcon else item.outlinedIcon,
                        tint = if (selected) Color.White else Color.Gray,
                        contentDescription = stringResource(item.label),
                        modifier = Modifier.size((24 * scale.value).dp)
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
                    indicatorColor = Color.DarkGray
                )
            )
        }
    }
}