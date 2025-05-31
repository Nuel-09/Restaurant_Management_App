package com.example.restaurantmanagementapp.Activity.CustomerActivity.Dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.restaurantmanagementapp.R

@Composable
@Preview
fun TopBar() {
    ConstraintLayout (
        modifier = Modifier
            .padding(top = 48.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ){
        val (name, settings,notification) = createRefs()
        Image(painter = painterResource(
            R.drawable.settings_icon),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(settings){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }.clickable {  }
        )
        Column (modifier = Modifier
            .constrainAs(name){
                top.linkTo(parent.top)
                start.linkTo(parent.end)
                end.linkTo(parent.start)
            }.padding(horizontal = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text ="Grace Tasty",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xF7D95854),
                modifier = Modifier
                    .padding(top = 32.dp)
                    .padding(horizontal = 65.dp)
            )
            Text(
                text ="Bites",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xF7D95854),
                modifier = Modifier
            )
        }
        Image(painter = painterResource(
            R.drawable.bell_icon),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(notification){
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }.clickable {  }
        )
    }
}