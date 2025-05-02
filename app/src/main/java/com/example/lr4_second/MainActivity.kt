package com.example.lr4_second

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lr4_second.databinding.ActivityMainBinding
import com.example.lr4_second.db.ExpenseItem
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.domain.model.ExpenseModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var navController = findNavController(R.id.nav_host_fragment)

        var bottomNavigationView = binding.mainBottomNavigation

        bottomNavigationView.setupWithNavController(navController)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}