package com.example.lr4_second.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lr4_second.R
import com.example.lr4_second.databinding.ActivityMainBinding
import com.example.lr4_second.databinding.FragmentAddingBinding
import com.example.lr4_second.viewModel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors

@AndroidEntryPoint
class AddingFragment: Fragment() {

    private val executor = Executors.newSingleThreadExecutor()
    private val viewModel: ExpenseViewModel by viewModels()

    private var _binding: FragmentAddingBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        executor.shutdown() // Важно: очистка ресурсов
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var btn = binding.addingButton
        var name = binding.addingNameText
        var sum = binding.addingSumText

        btn.setOnClickListener {
            viewModel.addExpense()

            Toast.makeText(requireContext(), "Добавлено: ${name.text}, ${sum.text}", Toast.LENGTH_SHORT).show()

            name.text.clear()
            sum.text.clear()
        }
    }
}