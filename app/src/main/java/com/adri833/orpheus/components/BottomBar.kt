package com.adri833.orpheus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.adri833.orpheus.R
import com.adri833.orpheus.ui.pulseScale
import com.adri833.orpheus.utils.noRippleClickable

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val bottomNavItems = BottomNavItemsProvider.getBottomNavItems()

    var pressedRoute by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            val isPressed = pressedRoute == item.route
            val scale = pulseScale(isPressed, 0.8f) {
                pressedRoute = null
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .noRippleClickable {
                        if (!selected) {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                        }
                        pressedRoute = item.route
                    }
                    .padding(vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size((24 * scale).dp)
                ) {
                    Icon(
                        painter = if (selected) item.filledIcon else item.outlinedIcon,
                        contentDescription = stringResource(item.label),
                        tint = if (selected) Color.White else Color.Gray,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = stringResource(item.label),
                    fontFamily = FontFamily(Font(R.font.merriweather_bold)),
                    fontSize = 10.sp,
                    color = if (selected) Color.White else Color.Gray
                )
            }
        }
    }
}