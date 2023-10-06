package com.talhakara.instagram.View

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.talhakara.instagram.ViewModel.ProfilViewModel

@Composable
fun  ProfilSayfasi(navController: NavController, viewModel: ProfilViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val posts by viewModel.posts.collectAsState() // ViewModel'den verileri alın
    val auth = Firebase.auth

    // Verileri çekmek için bir işlemi başlatın
    LaunchedEffect(Unit) {
        viewModel.posts
    }
    val state = rememberSwipeRefreshState(isRefreshing = false)


    SwipeRefresh(
        state = state,
        onRefresh = {
            // Yenileme mantığını burada uygulayabilirsiniz, örneğin ViewModel'den verileri yeniden yükleyebilirsiniz
            // viewModel.refreshData()
            state.isRefreshing = false
        }
    ) {

    Scaffold(

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(posts) { post ->
                PostItem(post = post)
            }
        }
    }
}}
