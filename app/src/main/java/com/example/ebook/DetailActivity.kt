package com.example.ebook

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DetailActivity : AppCompatActivity() {
    private lateinit var byauthor:TextView
    private lateinit var Publisher:TextView
    private lateinit var Category:TextView
    private lateinit var createshow:TextView
    private lateinit var textView9:TextView
    private lateinit var imageView2:ImageView
    private lateinit var tryread:Button
    private lateinit var cart:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val intent = intent
        val id_book = intent.getStringExtra("id_book")
        byauthor = findViewById(R.id.byauthor)
        Publisher = findViewById(R.id.Publisher)
        Category = findViewById(R.id.Category)
        createshow = findViewById(R.id.createshow)
        textView9 = findViewById(R.id.textView9)
        imageView2 = findViewById(R.id.imageView2)
        tryread = findViewById(R.id.tryread)
        cart = findViewById(R.id.cart)
        loadbookdetail(id_book.toString())
    }

    private fun loadbookdetail(id: String) {
        val queue = Volley.newRequestQueue(this@DetailActivity)
        val url = resources.getString(R.string.api_ip)+"/getbook_byid.php"
        val sharedPreference =  getSharedPreferences("Users", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()

        val request: StringRequest =
            object : StringRequest(Request.Method.POST, url, object : com.android.volley.Response.Listener<String?> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(response: String?) {
                    try {
                        val jsonObject = JSONObject(response)
                        Log.d("response",jsonObject.getString("error"))
                        if (jsonObject.getString("error").toString().equals("false")){
                            val dataArray = jsonObject.getJSONArray("data")
                            for (i in 0 until dataArray.length()) {
                                val dataobj = dataArray.getJSONObject(i)
                                byauthor.text = "โดย "+dataobj.getString("author").toString()
                                Publisher.text = "สำนักพิมพ์ "+dataobj.getString("publisher").toString()
                                Category.text = "หมวดหมู่ "+dataobj.getString("publisher").toString()
                                createshow.text = dataobj.getString("released").toString()
                                textView9.text = dataobj.getString("page").toString()+" หน้า"
                                Picasso.get()
                                    .load(resources.getString(R.string.api_ip)+"/"+dataobj.getString("img").toString())
                                    .resize(100, 100)
                                    .centerCrop()
                                    .into(imageView2)
                                tryread.setOnClickListener { view->
                                    val intent = Intent(this@DetailActivity, PdfActivity::class.java)
                                    startActivity(intent)
                                }
                                cart.setOnClickListener{ view->
                                    val intent = Intent(this@DetailActivity,Payment::class.java)
                                    startActivity(intent)
                                }
                            }
                        }else{
                            Toast.makeText(this@DetailActivity,jsonObject.getString("message").toString(),
                                Toast.LENGTH_LONG).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }, object : com.android.volley.Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.e("tag", "error is " + error!!.message)
                    Toast.makeText(this@DetailActivity, "Fail to update data..", Toast.LENGTH_SHORT)
                        .show()
                }
            }) {
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params.put("idbook", id)
                    return params
                }
            }
        queue.add(request)
    }
}