package com.example.lr4_second.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.example.lr4_second.R
import com.example.lr4_second.db.MainDB

class TableFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_table, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        var text = view.findViewById<TextView>(R.id.textView3)

        var textOfList = ""

        val db = MainDB.getDB(requireContext())
        db.getDao().getAllItems().asLiveData().observe(viewLifecycleOwner)
        { itList ->
            text.text = ""
            itList.forEach{
                text.append(it.expenseName + ": " + it.expenseValue + ";\n")
            }
        }


    }
}