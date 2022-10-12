package com.example.ebook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ebook.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
//    var beoktList  = arrayListOf<Beok>()

//    private val btnregis = findViewById<Button>(R.id.btnRegister)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreference =  getSharedPreferences("Users", Context.MODE_PRIVATE)
        val a = sharedPreference.getString("username","notfound")
        Log.d("a",a.toString())
        if (a != "notfound"){
            Log.d("IsLogin","true")
            val intent = Intent(this@MainActivity, home::class.java)
            startActivity(intent)
        }

        val btnregis:Button = findViewById(R.id.btnRegister)
        val loginbtn:Button = findViewById(R.id.btnLogin)
        val username:TextView = findViewById(R.id.edtUsername)
        val password:TextView = findViewById(R.id.edtPassword)
        loginbtn.setOnClickListener { view ->
            login(username.text.toString(),password.text.toString())
        }
        btnregis.setOnClickListener { view ->
            Toast.makeText(this,"asd",Toast.LENGTH_SHORT).show()
            addStudent()
        }
    }

    private fun login(username: String,password: String) {
        val queue = Volley.newRequestQueue(this@MainActivity)
        val url = resources.getString(R.string.api_ip)+"/login.php"
        val sharedPreference =  getSharedPreferences("Users", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()

        val request: StringRequest =
            object : StringRequest(Request.Method.POST, url, object : com.android.volley.Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    try {
                        val jsonObject = JSONObject(response)
                        Log.d("response",jsonObject.getString("error"))
                        if (jsonObject.getString("error").toString().equals("false")){
                            Toast.makeText(this@MainActivity,"เข้าสู่ระบบสำเร็จ",Toast.LENGTH_LONG).show()

                            val detail = jsonObject.getJSONObject("data")
                            editor.putString("id",detail.getString("id").toString())
                            editor.putString("username",detail.getString("username").toString())
                            editor.putString("fname",detail.getString("fname").toString())
                            editor.putString("lname",detail.getString("lname").toString())
                            editor.putString("email",detail.getString("email").toString())
                            editor.putString("phonenumber",detail.getString("phonenumber").toString())
                            editor.putString("gender",detail.getString("gender").toString())
                            editor.putString("user_type",detail.getString("user_type").toString())
                            editor.commit()
                            val intent = Intent(this@MainActivity, home::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this@MainActivity,jsonObject.getString("message").toString(),Toast.LENGTH_LONG).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }, object : com.android.volley.Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.e("tag", "error is " + error!!.message)
                    Toast.makeText(this@MainActivity, "Fail to update data..", Toast.LENGTH_SHORT)
                        .show()
                }
            }) {
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params.put("username", username)
                    params.put("password", password)
                    return params
                }
            }
        queue.add(request)
    }

    fun addStudent() {
        val intent = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(intent)
    }
}
