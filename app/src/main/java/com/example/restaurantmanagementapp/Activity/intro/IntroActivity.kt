package com.example.restaurantmanagementapp.Activity.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.Activity.BaseActivity
import com.example.restaurantmanagementapp.Activity.CustomerActivity.Dashboard.MainActivity
import com.example.restaurantmanagementapp.Activity.login.LoginActivity
import com.example.restaurantmanagementapp.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntroScreen(
                onGetStartedClick = {
                startActivity(Intent(this, MainActivity::class.java))
            },
                onSigninCick = {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            )
        }
    }
}

@Composable
@Preview
fun IntroScreen(onGetStartedClick: () -> Unit = {},
                onSigninCick: () -> Unit = {}
                ) {
    Box(modifier = Modifier.fillMaxSize())
    {
        Image(painter = painterResource(id = R.drawable.restaurant_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
        ) {
            val styledText= buildAnnotatedString {
                append("Welcome to ")
                withStyle(style = SpanStyle(color = colorResource(id = R.color.logo_color))){
                    append("GRACE TASTY BITES")
                }
                append(" where every")
                withStyle(style = SpanStyle(color = colorResource(R.color.logo_color))){
                    append("Bite ")
                }
                append("is a delight!")
            }
            Text(
                text = styledText,
                fontSize = 27.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 50.dp)
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = stringResource(R.string.introScreen_SubTitle),
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                lineHeight = 26.sp,
                modifier = Modifier
                    .padding(top = 50.dp)
                    .padding(horizontal = 16.dp)
            )
            GetStartedButton(onClick = onGetStartedClick,
                onSigninClick = onSigninCick,
                modifier = Modifier
                    .padding(top = 148.dp)
                )
        }
    }

}