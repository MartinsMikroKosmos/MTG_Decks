package com.example.mtg_decks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.mtg_decks.navigation.NavGraph
import com.example.mtg_decks.navigation.Screen
import com.example.mtg_decks.ui.theme.MTG_DecksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MTG_DecksTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    var selectedItem by remember { mutableStateOf(0) }

                    val items =
                        listOf(
                            "Search" to Icons.Default.Search,
                            "Random" to Icons.Default.Refresh,
                        )

                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, (title, icon) ->
                                    NavigationBarItem(
                                        icon = { Icon(icon, contentDescription = title) },
                                        label = { Text(title) },
                                        selected = selectedItem == index,
                                        onClick = {
                                            selectedItem = index
                                            when (index) {
                                                0 -> navController.navigate(Screen.Search.route)
                                                1 -> navController.navigate(Screen.Random.route)
                                            }
                                        },
                                    )
                                }
                            }
                        },
                    ) { innerPadding ->
                        NavGraph(navController)
                    }
                }
            }
        }
    }
}
