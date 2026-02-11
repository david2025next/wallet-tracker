package com.next.wallettracker.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(homeViewModel: HomeViewModel){

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeRoute(uiState = uiState)
}

@Composable
fun HomeRoute(uiState: HomeUiState){}