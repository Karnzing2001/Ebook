package com.example.ebook.ui.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ebook.DetailActivity
import com.example.ebook.R
import com.squareup.picasso.Picasso


class Ebook_Adapter(private val ebooklist: ArrayList<Ebok>, context: Context?):RecyclerView.Adapter<Ebook_Adapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,viewType:Int):Ebook_Adapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ebook_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:Ebook_Adapter.MyViewHolder,position:Int) {
        var currentitem = ebooklist[position]
        var imglink = "http://10.51.9.122/mbeok/"+currentitem.img
        holder.titlebook.text = currentitem.ebook_name
        holder.author.text = currentitem.author
        Log.d("IMG",imglink)
        Picasso.get()
            .load(imglink)
            .resize(100, 100)
            .centerCrop()
            .into(holder.imgbook)

        holder.cardView.setOnClickListener { view->
            Log.d("asdasdasd", currentitem.id.toString())

//            val context=holder.itemView.context
            val intent = Intent( view.context, DetailActivity::class.java)
            intent.putExtra("id_book",currentitem.id.toString())
            view.context.startActivity(intent)

//            val intent = Intent(view, home::class.java)
//            startActivity(intent)
        }
        holder.buttonprice.text = "฿"+currentitem.price

    }

    override fun getItemCount(): Int {
        return ebooklist.size
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val author:TextView = itemView.findViewById(R.id.author)
        val titlebook:TextView = itemView.findViewById(R.id.title_ebook)
        val imgbook:ImageView = itemView.findViewById(R.id.img)
        val cardView:CardView = itemView.findViewById(R.id.cardView)
        val buttonprice:Button = itemView.findViewById(R.id.buttonprice)
    }
}
