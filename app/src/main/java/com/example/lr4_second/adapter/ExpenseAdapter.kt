package com.example.lr4_second.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.R
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.domain.model.ExpenseModel
import com.example.lr4_second.viewModel.ExpenseViewModel

class ExpenseAdapter: RecyclerView.Adapter<ExpensesViewHolder>() {

    private var expensesList = ArrayList<ExpenseModel>()

    class ExpenseViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense_layout, parent, false)
        return ExpensesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.expenseName).text = expensesList[position].name
        holder.itemView.findViewById<TextView>(R.id.expenseValue).text = expensesList[position].expenseValue
    }

    fun updateItem(position: Int, newName: String, newValue: String, viewModel: ExpenseViewModel)
    {
        var name = expensesList.get(position).expName

        viewModel.updateExpense(name, newName, newValue)

        expensesList[position].name = newName
        expensesList[position].expenseValue = newValue
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int, viewModel: ExpenseViewModel)
    {
        var name = expensesList.get(position).expName

        viewModel.deleteExpense(name)

        expensesList.removeAt(position)

        notifyItemRemoved(position)
    }

    fun getList(): ArrayList<ExpenseModel>
    {
        return expensesList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<ExpenseModel>) {
        expensesList.clear()
        expensesList.addAll(newList)
        notifyDataSetChanged()
    }


    fun setList(list: ArrayList<ExpenseModel>)
    {
        expensesList = list
        notifyDataSetChanged()
    }
}