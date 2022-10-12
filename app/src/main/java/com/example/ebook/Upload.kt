package com.example.ebook
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.appcompat.app.AppCompatActivity
import com.example.ebook.databinding.UploadActivityBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
class Upload : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding: UploadActivityBinding
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    binding = UploadActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.UploadIcon.setOnClickListener {
            openImageChooser()
        }

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    binding.UploadIcon.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImage() {
        if (selectedImageUri == null) {
            Snackbar.make(binding.root,"กรุณาเลือกรูปที่ต้องการอัพโหลด!!",Snackbar.LENGTH_LONG).show()
            return
        }
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

       binding.progressbar.progress=0
        val body = UploadRequestBody(file, "image", this)
        uploadAPI().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), "")
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
              Snackbar.make(binding.root,"ไม่สามารถเชื่อมต่อกับเซิรฟ์เวอร์ได้ กรุณาลองใหม่ในภายหลัง",Snackbar.LENGTH_LONG).show()
                binding.progressbar.progress=0
            }
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    Snackbar.make(binding.root,"อัพโหลดเสร็จสิ้น",Snackbar.LENGTH_LONG).show()
                    binding.progressbar.progress=100
                    val intent = Intent(this@Upload, Uploadcompleat::class.java)
                    startActivity(intent)
                }
            }
        })

    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressbar.progress=percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }
    fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }
}
