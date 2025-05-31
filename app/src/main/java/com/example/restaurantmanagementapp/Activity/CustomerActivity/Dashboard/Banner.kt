package com.example.restaurantmanagementapp.Activity.CustomerActivity.Dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.restaurantmanagementapp.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
@Preview
fun BannerCarousel(
    modifier: Modifier = Modifier,

    bannerResIds: List<Int> = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3
    ),
    autoSlide: Boolean = true,
    slideIntervalMillis: Long = 3000L //3 Seconds
){
    val pagerState = rememberPagerState()
    //val coroutineScope = rememberCoroutineScope()  // to handle click events

    if(autoSlide) {
        LaunchedEffect(pagerState){
            while (true){
                kotlinx.coroutines.delay(slideIntervalMillis)
                val nextPage = (pagerState.currentPage + 1) % bannerResIds.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ){
        HorizontalPager(
            count = bannerResIds.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
            ) { page ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ){
                    Image(
                        painter = painterResource(id = bannerResIds[page]),
                        contentDescription = "Banner Image ${page +1}",
                        modifier = Modifier.fillMaxSize()
                        )
                    Spacer(modifier = Modifier.width(16.dp))
                }
        }
        Spacer(modifier = Modifier
            .width(12.dp)
            .height(8.dp) )

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = colorResource(R.color.indicator),
            inactiveColor = Color.White,
            indicatorWidth = 8.dp,
            spacing = 8.dp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

