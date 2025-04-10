package com.example.lr4_second.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.R
import com.example.lr4_second.adapter.ExpenseAdapter
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.model.ExpenseModel

class ExpensesFragment: Fragment() {

    lateinit var adapter: ExpenseAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_expenses, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        var list: ArrayList<ExpenseModel> = ArrayList()

        val db = MainDB.getDB(requireContext())
        db.getDao().getAllItems().asLiveData().observe(viewLifecycleOwner)
        { itList ->
            list = ArrayList()
            itList.forEach{
                var expense: ExpenseModel = ExpenseModel(it.expenseName, it.expenseValue)
                list.add(expense)
            }
            initial(view, list)
        }


    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId)
        {
            100 -> {
                showUpdateDialog(item.groupId)
                true
            }
            101 -> {
                adapter.deleteItem(item.groupId, requireContext())
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Метод для отображения диалога обновления
    private fun showUpdateDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Обновить расход")

        val input = EditText(requireContext())
        input.hint = "Название,сумма"

        builder.setView(input)

        builder.setPositiveButton("Обновить") { dialog, _ ->

            if (input.text.toString().isNotEmpty() && input.text.toString().contains(',') && input.text.toString().last() != ',') {

                var newExpenseName = input.text.toString().split(",")[0]
                var newExpenseValue = input.text.toString().split(",")[1]

                if (newExpenseValue.contains(' '))
                {
                    newExpenseValue = newExpenseValue.replace(" ", "")
                }

                if (newExpenseValue.all { it.isDigit() })
                {
                    if (newExpenseName.isNotEmpty() && newExpenseValue.isNotEmpty()) {
                        adapter.updateItem(position, newExpenseName, newExpenseValue, requireContext())
                    }
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Закрыть") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun initial(view: View, list: ArrayList<ExpenseModel>)
    {
        recyclerView = view.findViewById(R.id.expensesRv_expenses)
        adapter = ExpenseAdapter()
        recyclerView.adapter = adapter

        adapter.setList(list)
    }
}