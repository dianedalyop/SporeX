package com.example.sporex_app.network

data class ReplyResponse(
    val user_name: String,
    val content: String,
    val created_at: String?
)


enum class PostCategory {
    MOULD,
    HEALTH,
    MISC
}
data class CreatePostRequest(
    val user_name: String,
    val post_name: String,
    val content: String,
    val category: String
)

data class PostResponse(
    val id: String,
    val user_name: String,
    val post_name: String,
    val content: String,
    val created_at: String?,
    val replies: List<ReplyResponse>,
    val category: String,
    val image_url: String? = null
)


data class CreateReplyRequest(
    val user_name: String,
    val content: String
)

data class BasicResponse(
    val success: Boolean,
    val message: String
)
data class ReadingResponse(
    val device_id: String,
    val co2: Double,
    val temp_c: Double,
    val humidity: Double,
    val created_at: String
)


data class ScanResponse(
    val mould_detected: Boolean,
    val max_confidence: Double?
)

data class UpdateProfileResponse(
    val success: Boolean,
    val message: String,
    val username: String?,
    val email: String?,
    val profile_image: String?
)
