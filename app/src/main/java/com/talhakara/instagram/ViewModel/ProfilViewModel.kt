package com.talhakara.instagram.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.talhakara.instagram.View.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfilViewModel: ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val userRef: DatabaseReference = database.getReference("users")
    lateinit var GuncelKullaniciAdi:String



    init {
        // Kullanıcı oturum açmışsa ve kimlik doğrulama geçerliyse, kullanıcının UID'sini alın
        val currentUser: FirebaseUser? = auth.currentUser
        val userUID = currentUser?.uid

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //her postun kullanıcı adını kontrol et güncell kullanıcı ile eşlesenleri sadece diziye ekle

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
                            GuncelKullaniciAdi = dataSnapshot.child("username").value.toString()
                            // Kullanıcı adını kullanabilirsiniz
                              Log.d("Kullanıcı Adı", GuncelKullaniciAdi)
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



        if (userUID != null) {
            // Kullanıcının postlarını sakladığınız veritabanı yolunu belirtin
            val postsRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("posts")//.child(userUID)

            // Postları dinlemek için ValueEventListener ekleyin
            postsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val postList = mutableListOf<Post>()
                    for (postSnapshot in snapshot.children) {
                        if(postSnapshot.getValue(Post::class.java)!!.kullaniciAdi==GuncelKullaniciAdi){
                            val post = postSnapshot.getValue(Post::class.java) ?: Post()
                            postList.add(post)
                        }else{

                        }

                    }
                    _posts.value = postList
                }

                override fun onCancelled(error: DatabaseError) {
                    // Hata işleme kodu burada eklenebilir
                }
            })
        } else {
            // Kullanıcı oturum açmamışsa
        }
    }
}
