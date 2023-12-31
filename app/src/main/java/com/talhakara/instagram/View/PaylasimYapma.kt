package com.talhakara.instagram.View

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

@Composable
fun PaylasimYapma(navController: NavController) {
    var kullaniciAdi by remember { mutableStateOf("") }
    var aciklama by remember { mutableStateOf("") }
    var fotoUrl by remember { mutableStateOf("") }
    val storageRef = Firebase.storage.reference
    val context = LocalContext.current
    val auth = Firebase.auth
    // Veritabanı referansını alın
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val userRef: DatabaseReference = database.getReference("users")

    // Güncel kullanıcıyı alın
    val currentUser: FirebaseUser? = auth.currentUser

// Kullanıcı oturum açmışsa ve bir FirebaseUser nesnesi dönüyorsa, e-posta adresini alabilirsiniz
    if (currentUser != null) {
        val userGmail = currentUser.email
        if (userGmail != null) {
            // E-posta adresini kullanabilirsiniz
            // Örnek: Log.d("E-posta Adresi", email)
            // Kullanıcı adını çekmek için ValueEventListener kullanın
            val usernameRef: DatabaseReference = userRef.child(userGmail.replace(".", "_")) // Kullanıcı adını almak istediğiniz kullanıcının veritabanı referansını oluşturun
            usernameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        kullaniciAdi = dataSnapshot.child("username").value.toString()
                        // Kullanıcı adını kullanabilirsiniz
                        // Örnek: Log.d("Kullanıcı Adı", kullaniciAdi)
                    } else {
                        // Kullanıcı bulunamadı
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Hata durumunda burası çalışır
                }
            })

        } else {
            // E-posta adresi yoksa
        }
    } else {
        // Kullanıcı oturum açmamışsa
    }



    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            fotoUrl = uri.toString()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Siyah kareyi göster
        MyImageComponent(url = if (fotoUrl.isNotEmpty()) fotoUrl else "res/drawable/siyah_kare")

        Spacer(modifier = Modifier.height(16.dp))

        // Açıklama için OutlinedTextField
        OutlinedTextField(
            value = aciklama,
            onValueChange = { newValue ->
                aciklama = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Açıklama") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                launcher.launch("image/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Fotoğraf Seç")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (fotoUrl.isNotEmpty()) {
                    val storageReference = storageRef.child("images/${UUID.randomUUID()}")
                    val uploadTask = storageReference.putFile(Uri.parse(fotoUrl))

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            val indirilenFotoUrl = uri.toString()

                            // Kullanıcı adını kullanarak yeni bir gönderi oluşturun
                            val yeniPost = Post(kullaniciAdi, aciklama, indirilenFotoUrl)

                            // Firebase'e gönderiyi kaydetme işlemini çağırın
                            savePostToFirebase(yeniPost)

                            // Ana sayfaya yönlendirin
                            navController.navigate("AnaSayfa")

                            // Firebase yükleme başarılı oldu, Toast mesajı göster
                            Toast.makeText(context, "Fotoğraf başarıyla yüklendi!", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { exception ->
                            // Firebase yükleme sırasında hata oluştu
                            Toast.makeText(context, "Yükleme sırasında hata oluştu: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener { exception ->
                        // Firebase yükleme sırasında hata oluştu
                        Toast.makeText(context, "Yükleme sırasında hata oluştu: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Fotoğraf seçilmemişse hata mesajı veya işlem yapılabilir
                    Toast.makeText(context, "Lütfen bir fotoğraf seçin!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Paylaş")
        }


    }

}


fun savePostToFirebase(post: Post) {
    val database = Firebase.database
    val postsRef = database.getReference("posts")

    val postKey = postsRef.push().key // Yeni bir benzersiz post anahtarı alın

    if (postKey != null) {
        val postValues = post.toMap() // Post nesnesini bir veri haritasına çevirin
        // Post verisini Firebase veritabanına ekleyin
        postsRef.child(postKey).setValue(postValues)
    }
}



// Post sınıfınızın bir örneğini bir Map'e dönüştüren bir extension function
fun Post.toMap(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    map["kullaniciAdi"] = this.kullaniciAdi
    map["aciklama"] = this.aciklama
    map["fotoUrl"] = this.fotoUrl
    // Diğer alanları ekleyebilirsiniz

    return map
}
@Composable
fun MyImageComponent(url: String) {
    val painter = rememberImagePainter(data = url)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Kare boyutunu ayarlayın (isteğe bağlı)
            .background(color = if (url.isNotEmpty()) Color.Transparent else Color.Black) // Kare rengini siyah veya şeffaf olarak ayarlayın
    )
}

data class Post(val kullaniciAdi: String, var aciklama: String, var fotoUrl: String) {
    constructor() : this("", "", "") // No-argument constructor eklendi
}