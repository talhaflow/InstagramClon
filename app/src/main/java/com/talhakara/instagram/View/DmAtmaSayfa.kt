package com.talhakara.instagram.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun DmAtmaSayfa(navController: NavController) {
    val _users = remember { mutableStateListOf<String>() }

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val userRef: DatabaseReference = database.getReference("users")

    LaunchedEffect(userRef) {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _users.clear() // Önceki kullanıcıları temizle
                for (childSnapshot in snapshot.children) {
                    val user = childSnapshot.child("username").value
                    if (user is String) {
                        _users.add(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Hata durumunda yapılacak işlemler
            }
        })
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(_users) { username ->
            Text(text = "Kullanıcı Adı: $username", modifier = Modifier.padding(16.dp).clickable {
                navController.navigate("SohbetSayfa")
            })
        }
    }
}





