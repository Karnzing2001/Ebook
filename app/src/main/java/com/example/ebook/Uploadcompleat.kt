package com.example.ebook
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
class Uploadcompleat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploadcompleat)
        showGIF()
        val btn_click_me = findViewById(R.id.backhome) as Button
        btn_click_me.setOnClickListener{
            val intent = Intent(this,home::class.java)
            startActivity(intent)
        }
    }
    fun showGIF() {
        val imageView:ImageView = findViewById(R.id.Gifview)
        Glide.with(this).load(R.drawable.verify).into(imageView)
    }
}