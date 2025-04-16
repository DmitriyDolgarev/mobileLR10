package com.example.lr10_second.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lr10_second.R

class AddingFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_adding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        var text = view.findViewById<TextView>(R.id.textView)
    }
}