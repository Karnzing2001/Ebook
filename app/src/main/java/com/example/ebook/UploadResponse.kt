package com.example.ebook

data class UploadResponse(
    val error: Boolean,
    val message: String,
    val image: String
)