package com.next.wallettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.next.wallettracker.ui.screens.home.HomeRoute
import com.next.wallettracker.ui.screens.home.HomeViewModel
import com.next.wallettracker.ui.theme.WallettrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WallettrackerTheme {
                HomeRoute()
            }
        }
    }
}