package com.example.restaurantmanagementapp.Activity.Splash


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.restaurantmanagementapp.Activity.BaseActivity
import com.example.restaurantmanagementapp.Activity.intro.IntroActivity
import com.example.restaurantmanagementapp.R


class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashScreen()
            }

// Delay for 3 seconds and then start IntroActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
            }, 3000) // 3 seconds delay

    }
}

@Composable
@Preview
fun SplashScreen() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.black))
            .padding(top = 100.dp)
    ){
        // Your UI components here
        ConstraintLayout(modifier = Modifier
            .padding(top = 80.dp)
            .padding(horizontal = 60.dp)) {
            val(logoBackground)=createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(logoBackground) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(247.dp, 240.dp)
                    .background(color = Color(0xFFD5C6BC), shape = CircleShape)
                    .padding(horizontal = 32.dp)
            ){
                Image(painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize())
            }
        }
        Text(
            text ="Grace Tasty Bites",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xF7D95854),
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 65.dp)
        )
     }
}




