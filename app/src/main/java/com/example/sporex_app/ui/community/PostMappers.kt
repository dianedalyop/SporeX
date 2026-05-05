package com.example.sporex_app.ui.community

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.sporex_app.network.PostCategory
import com.example.sporex_app.network.PostResponse
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun PostResponse.toCommunityPost(): CommunityPost {
    val safeCategory = category.lowercase()

    return CommunityPost(
        id = id,
        author = user_name,
        content = content,
        timestamp = created_at ?: "Just now",
        category = when (safeCategory) {
            "mould" -> PostCategory.MOULD
            "health" -> PostCategory.HEALTH
            else -> PostCategory.MISC
        },
        imageUrl = image_url,
        comments = replies.mapIndexed { index, reply ->
            Comment(index, reply.user_name, reply.content)
        }.toMutableList()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestamp(dateString: String?): String {
    if (dateString.isNullOrBlank()) return "Just now"

    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME

        val postTime = LocalDateTime
            .parse(dateString, formatter)
            .atZone(ZoneOffset.UTC)

        val now = java.time.ZonedDateTime.now(ZoneId.systemDefault())
        val postLocal = postTime.withZoneSameInstant(ZoneId.systemDefault())

        val duration = Duration.between(postLocal, now)

        when {
            duration.toMinutes() < 1 -> "Just now"
            duration.toMinutes() < 60 -> "${duration.toMinutes()}m ago"
            duration.toHours() < 24 -> "${duration.toHours()}h ago"
            duration.toDays() < 7 -> "${duration.toDays()}d ago"
            else -> postLocal.toLocalDate().toString()
        }

    } catch (e: Exception) {
        "Just now"
    }
}