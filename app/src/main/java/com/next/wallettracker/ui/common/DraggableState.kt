package com.next.wallettracker.ui.common

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

enum class DragAction {
    Center,
    EndRevealed
}

@Composable
fun rememberAnchoredDraggableState() : AnchoredDraggableState<DragAction> {
    val density = LocalDensity.current
    val actionWidth = with(density) { 70.dp.toPx() }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    return remember {
        AnchoredDraggableState(
            initialValue = DragAction.Center,
            positionalThreshold = { d -> d * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec,
            anchors = DraggableAnchors {
                DragAction.Center at 0f
                DragAction.EndRevealed at -actionWidth
            }
        )
    }
}