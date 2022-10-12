package com.example.ebook
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.ebook.databinding.PaymentBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream



class Payment : AppCompatActivity() {
    private lateinit var binding: PaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        title = "ชำระเงิน"
        super.onCreate(savedInstanceState)
        binding = PaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnsave.setOnClickListener {
            val bitmap = binding.imageView2.getDrawable().toBitmap()
            var outStream: FileOutputStream? = null
            val sdCard = Environment.getExternalStorageDirectory()
            val dir = File(sdCard.absolutePath + "/Download")
            dir.mkdirs()
            val fileName = String.format("%d.jpg", System.currentTimeMillis())
            val outFile = File(dir, fileName)
            outStream = FileOutputStream(outFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
            Snackbar.make(binding.root, "บันทึก QR CODE แล้ว", Snackbar.LENGTH_LONG).show()
        }

        binding.butnext.setOnClickListener {
            val intent = Intent(this, Upload::class.java)
            startActivity(intent)
        }
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val bankno = findViewById<View>(R.id.bankno) as TextView
        val clipData = ClipData.newPlainText("label", bankno.text.toString())
        clipboardManager.setPrimaryClip(clipData)
        val snackbar: Snackbar = Snackbar.make(binding.root, "คัดลอกเลขที่บัญชีอัตโนมัติแล้ว", Snackbar.LENGTH_LONG)
        snackbar.show()
        }
    }




