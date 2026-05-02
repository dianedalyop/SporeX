package com.example.sporex_app.ui.community

import com.example.sporex_app.network.PostCategory


data class Post(
    val id: String,
    val author: String,
    val content: String,
    val timestamp: String,
    val category: String = "misc",
)

data class Comment(
    val id: Int,
    val author: String,
    val content: String
)

data class CommunityPost(
    val id: String,
    val author: String,
    val content: String,
    val timestamp: String,
    val category: PostCategory,
    var likes: Int = 0,
    var isLiked: Boolean = false,
    val comments: MutableList<Comment> = mutableListOf()
)
