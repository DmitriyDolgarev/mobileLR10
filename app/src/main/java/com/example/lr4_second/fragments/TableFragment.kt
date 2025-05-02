package com.example.lr4_second.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lr4_second.R
import com.example.lr4_second.databinding.FragmentTableBinding
import com.example.lr4_second.viewModel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TableFragment: Fragment() {

    private val viewModel: ExpenseViewModel by viewModels()

    private var _binding: FragmentTableBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTableBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        var text = binding.textView3

        viewLifecycleOwner.lifecycleScope.launch {

            text.text = ""
            viewModel.loadExpenses()
        }

    }
}