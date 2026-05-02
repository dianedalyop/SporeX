package com.example.sporex_app.useraccount

import android.content.Context
import android.net.Uri
import com.example.sporex_app.network.BasicResponse
import com.example.sporex_app.network.SporexApi
import com.example.sporex_app.network.UpdateProfileResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

object UserRepository {

    suspend fun updateProfile(
        api: SporexApi,
        context: Context,
        email: String,
        username: String,
        imageUri: Uri?
    ): Response<UpdateProfileResponse> {

        val emailBody = email.toRequestBody("text/plain".toMediaType())
        val usernameBody = username.toRequestBody("text/plain".toMediaType())

        val imagePart = imageUri?.let { uri ->
            val inputStream = context.contentResolver.openInputStream(uri)!!
            val tempFile = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }

            val requestFile = tempFile.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData(
                "file",
                tempFile.name,
                requestFile
            )
        }

        return api.updateProfile(emailBody, usernameBody, imagePart)
    }
}