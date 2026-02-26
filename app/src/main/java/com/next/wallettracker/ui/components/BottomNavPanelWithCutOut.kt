package com.next.wallettracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.next.wallettracker.ui.common.BottomNavShape
import com.next.wallettracker.ui.navigation.TOP_LEVEL_ROUTES
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun BoxScope.BottomNavPanelWithCutOut() {
  Box(
    modifier = Modifier
      .align(Alignment.BottomCenter)
      .fillMaxWidth()
      .height(64.dp)
      .clip(
        BottomNavShape(
          cornerRadius = with(LocalDensity.current) { 0.dp.toPx() },
          dockRadius = with(LocalDensity.current) { 48.dp.toPx() },
        ),
      ) // Apply the custom shape
      .background(Color.Blue)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth(),
        //.padding(horizontal = 56.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Navigation icons (left and right of the cutout)

      TOP_LEVEL_ROUTES.forEach { (topLevelDestination, bottomItem) ->
        NavigationBarItem(
          selected = true,
          onClick = {
            //onSelectedKey(topLevelDestination)
          },
          icon = {
            Icon(
              imageVector = bottomItem.unselectedIcon,
              stringResource(bottomItem.title)
            )
          },
          label = {
            Text(stringResource(bottomItem.title))
          }
        )
      }
    }
  }
}

@Preview
@Composable
private fun Test(){
  Box{
    BottomNavPanel()
  }
}

@Composable
fun BoxScope.BottomNavPanel() {
  Box(
    modifier = Modifier
      .align(Alignment.BottomCenter)
      .fillMaxWidth()
  ) {
    BottomNavPanelWithCutOut()

    // Floating button positioned over the cutout
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 32.dp)
        .size(58.dp)
        .clip(CircleShape)
        .background(Color.Blue),
      contentAlignment = Alignment.Center,
    ) {
      // Your central action button (e.g., the camera icon)
      FloatingActionButton(
        onClick = {}
      ) {
        Icon(
          Icons.Default.Add, null
        )
      }
    }
  }
}