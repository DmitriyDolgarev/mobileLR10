package com.example.lr4_second.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.adapter.ExpenseAdapter
import com.example.lr4_second.adapter.ImageAdapter
import com.example.lr4_second.domain.model.ExpenseModel
import com.example.lr4_second.domain.model.ImageModel

@BindingAdapter("app:expenseItems")
fun RecyclerView.setExpenseItems(items: List<ExpenseModel>?) {
    val adapter = this.adapter as? ExpenseAdapter ?: return
    items?.let {
        adapter.submitList(ArrayList(it)) // Безопасное создание нового ArrayList
    }
}

@BindingAdapter("app:expenseList")
fun TextView.setExpenseList(items: List<ExpenseModel>?) {
    text = items?.joinToString("\n") { "${it.expName}: ${it.expValue}" } ?: ""
}

@BindingAdapter("app:imageItems")
fun RecyclerView.setImageItems(items: List<ImageModel>?) {
    (adapter as? ImageAdapter)?.submitList(items ?: emptyList())
}