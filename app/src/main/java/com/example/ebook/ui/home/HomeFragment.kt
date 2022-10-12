package com.example.ebook.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ebook.R
import com.example.ebook.databinding.FragmentHomeBinding
import org.json.JSONException
import org.json.JSONObject


private lateinit var adapter:Ebook_Adapter
private lateinit var recyclerView: RecyclerView
private lateinit var ebookArrayBook:ArrayList<Ebok>

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1:String,param2:String) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString("AAA",param1)
                putString("BBB",param2)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datainitialize()
        val layoutManager = GridLayoutManager(this.context,2)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = Ebook_Adapter(ebookArrayBook, context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun datainitialize(){
        ebookArrayBook = arrayListOf<Ebok>()
        var api = resources.getString(R.string.api_ip)+"/getall_ebook.php"
        val stringRequest = StringRequest(
        Request.Method.GET, api,
        { response ->
            try {
                val obj = JSONObject(response)
                if (obj.optString("error") == "false") {
                    val dataArray = obj.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val dataobj = dataArray.getJSONObject(i)
                        ebookArrayBook.add(Ebok(
                            dataobj.getString("ebook_id").toInt(),
                            dataobj.getString("ebook_name").toString(),
                            dataobj.getString("img").toString(),
                            dataobj.getString("publisher").toString(),
                            dataobj.getString("author").toString(),
                            dataobj.getString("file").toString(),
                            dataobj.getString("price").toString(),
                            dataobj.getString("released").toString(),
                            dataobj.getString("page").toString(),
                            dataobj.getString("created_at").toString(),
                            dataobj.getString("updated_at").toString(),
                        ))
                        recyclerView.setAdapter(Ebook_Adapter(ebookArrayBook,this.context))
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
                Log.d("edd",e.toString())
            }
        },
        { error ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            Log.d("aaa",error.toString())
        })

        val requestQueue = Volley.newRequestQueue(context)

        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

