package com.example.lr4_second.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.R
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.domain.model.ExpenseModel
import com.example.lr4_second.domain.model.ImageModel
import com.example.lr4_second.viewModel.ExpensePhotosViewModel
import com.example.lr4_second.viewModel.ExpenseViewModel

class ImageAdapter: RecyclerView.Adapter<ImagesViewHolder>() {

    private var imageList = ArrayList<ImageModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_layout, parent, false)
        return ImagesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.itemView.findViewById<ImageView>(R.id.itemImg).setImageURI(Uri.parse(imageList[position].uri))
    }

    fun deleteItem(position: Int, context: Context, viewModel: ExpensePhotosViewModel)
    {
        var id = imageList.get(position).id

        viewModel.deleteExpensePhoto(id)

        imageList.removeAt(position)

        notifyItemRemoved(position)
    }

    fun getList(): ArrayList<ImageModel>
    {
        return imageList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<ImageModel>) {
        imageList.clear()
        imageList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setList(list: ArrayList<ImageModel>)
    {
        imageList = list
        notifyDataSetChanged()
    }
}