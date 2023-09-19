package com.talhakara.instagram.View


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun kayitOl(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var kullaniciAdi by remember { mutableStateOf("") }
    val context = LocalContext.current // Bu satır, compose içindeki bağlamı alır. Gerekirse uygun şekilde ayarlayın.

    // Firebase Authentication nesnesini alın
    val auth = Firebase.auth

    fun saveUsernameToFirebase(username: String, kullaniciAdi: String) {
        val database = Firebase.database
        val userRef = database.getReference("users")

        // Kullanıcı adını kullanarak bir veri oluşturun
        val user = hashMapOf(
            "username" to kullaniciAdi  // Kullanıcı adını kullaniciAdi olarak kaydedin
        )

        // Kullanıcı verisini Firebase veritabanına ekleyin
        userRef.child(username.replace(".", "_")) // E-posta adresi içeriyorsa '.' karakterini '_'' ile değiştirin
            .setValue(user)
            .addOnSuccessListener {
                // Kullanıcı adı başarıyla kaydedildiğinde yapılacak işlemler
                // Örneğin, ana sayfaya yönlendirme yapabilirsiniz
                navController.navigate("anaSayfa")
            }
            .addOnFailureListener { exception ->
                // Hata durumunda kullanıcıyı bilgilendirebilirsiniz
                Toast.makeText(
                    context,
                    "Kullanıcı adı kaydetme başarısız oldu. Hata: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Instagram Kayıt ol",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        //kullanıcı adı alma
        OutlinedTextField(
            value = kullaniciAdi,
            onValueChange = { kullaniciAdi = it },
            placeholder = { Text("Kullanıcı Adı") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Mail") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
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

        // Kayıt Ol Button
        Button(
            onClick = {
                if (username.isEmpty() || password.isEmpty() || kullaniciAdi.isEmpty()) {
                    Toast.makeText(context, "Zorunlu boşlukları doldurun", Toast.LENGTH_SHORT).show()
                } else {
                    // Kullanıcıyı Firebase'e kaydet
                    auth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Kayıt başarılıysa, kullanıcı adını Firebase veritabanına kaydedin
                                saveUsernameToFirebase(username, kullaniciAdi)
                            } else {
                                // Kayıt başarısızsa, hata mesajını gösterin
                                Toast.makeText(
                                    context,
                                    "Kayıt olma başarısız oldu. Hata: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Kayıt ol")
        }

    }



}


