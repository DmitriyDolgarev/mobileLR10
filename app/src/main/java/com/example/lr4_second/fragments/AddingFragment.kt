package com.example.lr4_second.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.example.lr4_second.R
import com.example.lr4_second.db.ExpenseItem
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.model.ExpenseModel

class AddingFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_adding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)



        var btn = view.findViewById<Button>(R.id.addingButton)
        var name = view.findViewById<EditText>(R.id.addingNameText)
        var sum = view.findViewById<EditText>(R.id.addingSumText)

        var list: ArrayList<ExpenseModel> = ArrayList()

        val db = MainDB.getDB(requireContext())
        db.getDao().getAllItems().asLiveData().observe(viewLifecycleOwner)
        { itList ->
            list = ArrayList()
            itList.forEach{
                var expense: ExpenseModel = ExpenseModel(it.expenseName, it.expenseValue)
                list.add(expense)
            }
        }

        btn.setOnClickListener {
            var text = makeText(name.text.toString(), sum.text.toString())
            var expense = ExpenseModel(name.text.toString(), sum.text.toString())

            /*db

             */
            val item = ExpenseItem(null, name.text.toString(), sum.text.toString())
            Thread{
                db.getDao().insertItem(item)
            }.start()

            list.add(expense)

            name.setText("")
            sum.setText("")

            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        }
    }
    fun makeText(name: String, sum: String): String
    {
        var text = ""
        var sumArr = sum.toCharArray()

        if ((name == "") || (sum == ""))
        {
            text = "Заполните поля ввода!"
        }
        else
        {
            if (sumArr[0] == '-')
            {
                text = "Сумма должна быть положительной!"
            }
            else
            {
                text = "Добавлено: " + name + ", " + sum
            }
        }
        return text
    }
}