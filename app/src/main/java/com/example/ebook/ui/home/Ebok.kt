package com.example.ebook.ui.home

import android.provider.ContactsContract

data class Ebok(
    val id: Int,
    val ebook_name: String,
    val img: String,
    val publisher: String,
    val author: String,
    val file: String,
    val price: String,
    val released: String,
    val page: String,
    val created_at: String,
    val updated_at: String,
    )
