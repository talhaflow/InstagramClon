package com.talhakara.instagram.View


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.talhakara.instagram.ViewModel.PostViewModel
import com.talhakara.instagram.ui.theme.InstagramTheme

@Preview(showBackground = true)
@Composable
fun Goster() {
    InstagramTheme {
       // AnaSayfa(navController, viewModel) // AnaSayfa'yı çağırın
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnaSayfa(navController: NavController, viewModel: PostViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val posts by viewModel.posts.collectAsState() // ViewModel'den verileri alın
    val auth = Firebase.auth



    // Verileri çekmek için bir işlemi başlatın
    LaunchedEffect(Unit) {
        viewModel.posts
    }
/*TopAppBar(
                title = {
                    Text(text = "Exit") // İstediğiniz başlık metnini kullanabilirsiniz
                },
                actions = {
                    IconButton(
                        onClick = {
                            auth.signOut()
                            navController.navigate("loginSayfa")

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Çıkış Yap"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )*/



    Scaffold(
        bottomBar = {

            NavigationBar(
               // colors = MaterialTheme.colorScheme.primary,
               // shadowElevation = 0.dp
            ) {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Keşfet")
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "DM")
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navController.navigate("paylasSayfa")

                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Paylaş")
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Hesabım")
                    }
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { auth.signOut()
                        navController.navigate("loginSayfa")},
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Hesabım")
                    }
                )

                // Diğer navigasyon öğelerini burada ekleyebilirsiniz
            }
        }
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
}
@Composable
fun PostItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = post.kullaniciAdi, fontWeight = FontWeight.Bold)
        Text(text = post.aciklama)
        Image(
            painter = rememberImagePainter(data = post.fotoUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}