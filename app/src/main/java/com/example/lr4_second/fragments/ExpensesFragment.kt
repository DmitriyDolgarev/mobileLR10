package com.example.lr4_second.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.R
import com.example.lr4_second.adapter.ExpenseAdapter
import com.example.lr4_second.databinding.FragmentAddingBinding
import com.example.lr4_second.databinding.FragmentExpensesBinding
import com.example.lr4_second.domain.model.ExpenseModel
import com.example.lr4_second.viewModel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpensesFragment: Fragment() {

    lateinit var adapter: ExpenseAdapter
    lateinit var recyclerView: RecyclerView

    private val viewModel: ExpenseViewModel by viewModels()

    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        recyclerView = binding.expensesRvExpenses
        adapter = ExpenseAdapter()
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadExpenses()
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
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.deleteItem(item.groupId, viewModel)
                }
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
                        viewLifecycleOwner.lifecycleScope.launch {
                            adapter.updateItem(position, newExpenseName, newExpenseValue, viewModel)
                        }
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
}