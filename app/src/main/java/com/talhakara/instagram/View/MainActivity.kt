package com.talhakara.instagram.View

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.talhakara.instagram.ViewModel.PostViewModel
import com.talhakara.instagram.ViewModel.ProfilViewModel
import com.talhakara.instagram.ui.theme.InstagramTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstagramTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   SayfaGecis()

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstagramTheme {
        SayfaGecis()
    }
}
@Composable
fun SayfaGecis() {
    var navController = rememberNavController()
    val postViewModel = viewModel<PostViewModel>()
    val ProfilViewModel=viewModel<ProfilViewModel>()
    NavHost(navController = navController, startDestination = "loginSayfa") {
        composable("loginSayfa") {
            LoginScreen(navController = navController)
        }
        composable("kayitSayfa") {
            kayitOl(navController = navController)
        }
        composable("anaSayfa") { // Değiştirdim: Aynı hedef ismi kullanılamaz
            AnaSayfa(navController = navController, viewModel =postViewModel )
        }
        composable("paylasSayfa"){
            PaylasimYapma(navController = navController)
        }
        composable("DmSayfa"){
            DmSayfasi(navController = navController)
        }
        composable("ProfilSayfa"){
            ProfilSayfasi(navController = navController,viewModel =ProfilViewModel)
        }

    }
}



@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current // Bu satır, compose içindeki bağlamı alır. Gerekirse uygun şekilde ayarlayın.


    val currentUser = FirebaseAuth.getInstance().currentUser // Check if a user is already signed in.

    if (currentUser != null) {
        // If a user is already signed in, navigate them directly to the main page.
        navController.navigate("anaSayfa")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Instagram Clone",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))
        //kullanıcı adı alma
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Kullanıcı Adı") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        //şifre alma
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Şifre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        //giriş yap button
        Button(
            onClick = {
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(context, "Zorunlu boşlukları doldurun", Toast.LENGTH_SHORT).show()
                }else{
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Giriş başarılı
                                val user = FirebaseAuth.getInstance().currentUser
                                // Kullanıcıyı diğer ekrana yönlendirin veya işlemlerinizi gerçekleştirin.
                                navController.navigate("anaSayfa")
                            } else {
                                // Giriş başarısız
                                Toast.makeText(context, "Giriş başarısız oldu.", Toast.LENGTH_SHORT).show()
                            }
                        }

                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Giriş Yap")
        }

       //kayıt ol button
        Button(
            onClick = {
                navController.navigate("kayitSayfa")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Kayıt Ol")
        }


    }
}
