package com.example.ebook

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.io.File


private lateinit var pdfview:WebView
class PdfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        pdfview = findViewById(R.id.webview)

        pdfview.webViewClient = WebViewClient()

        // this will load the url of the website
        pdfview.loadUrl("https://docs.google.com/gview?embedded=true&url=http://www.pdf995.com/samples/pdf.pdf")
//        pdfview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "https://drive.google.com/file/d/1dey7inqpDDkSiFHEi1M8qQvAf0yCYMvh/view");

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        pdfview.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        pdfview.settings.setSupportZoom(true)
    }
}