package com.talhakara.instagram.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.talhakara.instagram.View.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("posts")

    init {
        // Verileri Firebase'den dinlemek için ValueEventListener ekleyin
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = mutableListOf<Post>()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java) ?: Post() // No-argument constructor ile oluşturulmuş bir Post nesnesi kullanılıyor
                    postList.add(post)
                }
                _posts.value = postList
            }

            override fun onCancelled(error: DatabaseError) {
                // Hata işleme kodu burada eklenebilir
            }
        })
    }

    fun addPost(post: Post) {
        val postKey = databaseRef.push().key
        if (postKey != null) {
            databaseRef.child(postKey).setValue(post)
        }
    }
}

