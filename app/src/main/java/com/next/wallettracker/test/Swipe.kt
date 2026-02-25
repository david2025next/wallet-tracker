package com.next.wallettracker.test

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class SlideToAction {
    Center, StartRevealed, EndRevealed
}

@Preview(showBackground = true)
@Composable
fun SwipeTest(modifier: Modifier = Modifier) {

    val actionDensityDp = 80.dp
    val density = LocalDensity.current
    val actionDensity = with(density) { 80.dp.toPx() }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = SlideToAction.Center,
            velocityThreshold = { 1000f },
            positionalThreshold = { d -> d * 0.4f },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec

        ).apply {
            updateAnchors(
                DraggableAnchors {
                    SlideToAction.StartRevealed at actionDensity
                    SlideToAction.Center at 0f
                    SlideToAction.EndRevealed at -actionDensity
                }
            )
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .height(70.dp),
    ) {

        Box(
            Modifier
                .align(Alignment.CenterStart)
                .width(actionDensityDp)
                .fillMaxHeight()
                .background(Color.Blue),
        ) {
            Icon(Icons.Default.Edit, null)
        }

        Box(
            Modifier
                .align(Alignment.CenterEnd)
                .width(actionDensityDp)
                .fillMaxHeight()
                .background(Color.Red),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(Icons.Default.Delete, null)
        }
        Box(
            Modifier
                .offset {
                    IntOffset(
                        dragState.requireOffset().roundToInt(), 0
                    )
                }
                .anchoredDraggable(
                    dragState, Orientation.Horizontal
                )
                .background(Color.DarkGray)
                .fillMaxSize()
        ) {
            Text(
                "Task", modifier = Modifier.padding(16.dp)
            )
        }
    }
}