package com.example.ebook


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ebook.databinding.ActivityRegisterBinding
import org.json.JSONException
import org.json.JSONObject
import javax.security.auth.callback.Callback

class RegisterActivity : AppCompatActivity() {
    private lateinit var bindingInsert : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingInsert = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bindingInsert.root)
        val username:TextView = findViewById(R.id.username)
        val password:TextView = findViewById(R.id.password)
        val confirmpassword:TextView = findViewById(R.id.confirmpassword)
        val email:TextView = findViewById(R.id.email)
        val phonenumber:TextView = findViewById(R.id.tel)
        val fname:TextView = findViewById(R.id.name)
        val lname:TextView = findViewById(R.id.lastname)
        val gender:AutoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val btnregis:Button = findViewById(R.id.submit)
        val genderarray = resources.getStringArray(R.array.gender)

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, genderarray)
        gender.setAdapter(adapter)

        btnregis.setOnClickListener { view->
            Toast.makeText(this,username.text.toString(),Toast.LENGTH_SHORT).show()
            val pass = password.text.toString()
            val cpass = confirmpassword.text.toString()
            if (pass.equals(cpass)) {
                addData(
                    username.text.toString(),
                    password.text.toString(),
                    confirmpassword.text.toString(),
                    email.text.toString(),
                    phonenumber.text.toString(),
                    fname.text.toString(),
                    lname.text.toString(),
                    gender.text.toString()
                )
            }else{
                Toast.makeText(this,"Pass ไม่ตรงกัน",Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun addData(username: String,password: String,confirmpassword: String,email: String,
                        phonenumber:String,fname:String,lname:String,gender:String) {
        val queue = Volley.newRequestQueue(this@RegisterActivity)
        val url = resources.getString(R.string.api_ip)+"/register.php"
        val request: StringRequest =
            object : StringRequest(Request.Method.POST, url, object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    Toast.makeText(this@RegisterActivity, "Data Updated..", Toast.LENGTH_SHORT).show()
                    try {
                        val jsonObject = JSONObject(response)
                        Log.d("response",jsonObject.getString("error"))
                        if (jsonObject.getString("error").toString().equals("false")){
                            Toast.makeText(this@RegisterActivity,"สมัครสมาชิกสำเร็จ",Toast.LENGTH_LONG).show()
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this@RegisterActivity,jsonObject.getString("message").toString(),Toast.LENGTH_LONG).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.e("tag", "error is " + error!!.message)
                    Toast.makeText(this@RegisterActivity, "Fail to update data..", Toast.LENGTH_SHORT)
                        .show()
                }
            }) {
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params.put("username", username)
                    params.put("password", password)
                    params.put("email", email)
                    params.put("tel", phonenumber)
                    params.put("fname", fname)
                    params.put("lname", lname)
                    params.put("gender", gender)
                    params.put("user_type", "member")
                    return params
                }
            }
        queue.add(request)
    }

}