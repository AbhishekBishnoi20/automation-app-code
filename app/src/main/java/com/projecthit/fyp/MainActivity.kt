package com.projecthit.fyp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.projecthit.artful.constants.Screen

import com.projecthit.fyp.screens.ControlScreen
import com.projecthit.fyp.screens.InfoScreen

import com.projecthit.fyp.ui.theme.FYPTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            FYPTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var selectedNav by remember { mutableStateOf(Screen.Home.route) }


                    Scaffold(
                        bottomBar = {
                            BottomNavBar(selectedNav) { route ->
                                selectedNav = route
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true // Clears the entire back stack and avoids any back stack entry
                                    }
                                }
                            }
                        }
                    ) { paddingValues ->
                        val pv = paddingValues.calculateBottomPadding()

                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, pv)
                        ) {
                            composable(Screen.Info.route
                            ) {
                                InfoScreen()
                            }
                            composable(Screen.Home.route) {
                                ControlScreen()
                            }

                        }
                    }
                }
            }
        }

    }
}


@Composable
fun BottomNavBar(selectedNav: String, onSelectNav: (String) -> Unit) {
    val items = listOf("home", "info") // Replace with your actual navigation items

    BottomAppBar(
        modifier = Modifier.heightIn(min = 56.dp), // Recommended minimum touch target size
        containerColor = Color.Transparent
    ) {
        items.forEach { item ->
            val selected = selectedNav == item
            val icon = if (item == "home") R.drawable.home else R.drawable.info // Replace with actual resource IDs

            NavigationBarItem(
                selected = selected,
                onClick = { onSelectNav(item) },
                icon = {
                    // Apply tint based on selection state
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = if (selected) Color.White else Color.White.copy(alpha = 0.4f)
                    )
                },
                label = { if (selected) Text(text = item.uppercase()) },
                // Define custom colors here
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.White.copy(alpha = 0.4f),
                    unselectedTextColor = Color.White.copy(alpha = 0.4f)
                ),
                alwaysShowLabel = false
            )
        }
    }
}