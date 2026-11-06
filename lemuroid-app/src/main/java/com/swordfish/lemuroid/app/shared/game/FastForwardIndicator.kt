package com.swordfish.lemuroid.app.shared.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swordfish.lemuroid.R

@Composable
internal fun FastForwardIndicator(
    speed: Int,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.game_fast_forward_indicator, speed)
    val active = speed > 1
    val actionLabel = stringResource(if (active) R.string.game_fast_forward_stop else R.string.game_fast_forward_resume)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val shape = RoundedCornerShape(6.dp)
    Box(
        modifier =
            modifier
                .safeDrawingPadding()
                .padding(8.dp)
                .clip(shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = androidx.compose.foundation.LocalIndication.current,
                    role = Role.Button,
                    onClickLabel = actionLabel,
                    onClick = onToggle,
                )
                .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                .semantics(mergeDescendants = true) { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                Modifier
                    .background(
                        if (pressed) Color.DarkGray else Color.Black.copy(alpha = if (active) 0.45f else 0.25f),
                        shape,
                    )
                    .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Icon(
                Icons.Default.FastForward,
                contentDescription = null,
                tint =
                    Color.White.copy(
                        alpha =
                            if (pressed) {
                                1f
                            } else if (active) {
                                0.8f
                            } else {
                                0.5f
                            },
                    ),
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = "$speed×",
                color =
                    Color.White.copy(
                        alpha =
                            if (pressed) {
                                1f
                            } else if (active) {
                                0.8f
                            } else {
                                0.5f
                            },
                    ),
                fontSize = 11.sp,
            )
        }
    }
}
