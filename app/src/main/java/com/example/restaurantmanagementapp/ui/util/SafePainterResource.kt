
package com.example.restaurantmanagementapp.ui.util

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Composable
fun safePainterResource(
    @DrawableRes resId: Int?,
    @DrawableRes fallbackResId: Int
): Painter {
    // Only use resId if it's not null, not zero, and positive
    val validResId = if (resId != null && resId != 0) resId else fallbackResId
    return painterResource(id = validResId)
}