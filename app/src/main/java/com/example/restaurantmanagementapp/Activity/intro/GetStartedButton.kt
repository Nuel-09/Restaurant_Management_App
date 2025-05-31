package com.example.restaurantmanagementapp.Activity.intro

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun GetStartedButton(onClick: () -> Unit = {},
                     onSigninClick: () -> Unit = {},
                     modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ){
        Button(onClick = { onSigninClick()},
            colors = ButtonDefaults.buttonColors( containerColor = Color.Transparent),
            shape = RoundedCornerShape(50.dp),
            modifier = modifier
                .padding(end = 16.dp)
                .fillMaxWidth(0.35f)
                .height(50.dp)
                .border(1.dp, Color.White, shape = RoundedCornerShape(50.dp))
        ) {
            Text(text = "Signin",
                fontSize = 18.sp,
                color = Color.White
                )
        }
        Button(onClick = { onClick()},
            colors = ButtonDefaults.buttonColors( containerColor = Color(0xF7E03F3A)),
            shape = RoundedCornerShape(50.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Let's Get Started",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}