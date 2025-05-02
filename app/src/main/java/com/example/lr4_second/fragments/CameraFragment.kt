package com.example.lr4_second.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.R
import com.example.lr4_second.adapter.ImageAdapter
import com.example.lr4_second.databinding.FragmentCameraBinding
import com.example.lr4_second.domain.model.ImageModel
import com.example.lr4_second.viewModel.ExpensePhotosViewModel
import com.example.lr4_second.viewModel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.OutputStream

@AndroidEntryPoint
class CameraFragment: Fragment() {

    private val requestCodeTakePhoto = 1
    private lateinit var spinner: Spinner

    private lateinit var listOfItems: ArrayList<String>
    private lateinit var idOfExpenses: HashMap<String, Int>

    private lateinit var selectedItem: String

    private lateinit var imageList: ArrayList<ImageModel>

    lateinit var adapter: ImageAdapter
    lateinit var recyclerView: RecyclerView

    lateinit var view1: View

    private val expenseViewModel: ExpenseViewModel by viewModels()
    private val photosViewModel: ExpensePhotosViewModel by viewModels()

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        spinner = binding.cameraSpinner
        listOfItems = ArrayList()
        idOfExpenses = HashMap()
        imageList = ArrayList()

        loadExpenses()

        selectedItem = ""

        view1 = view

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = listOfItems.get(position)

                Log.e("SELECTED_ITEM", selectedItem)

                makeListOfImages(view1)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }


        var takePhotoBtn = binding.cameraButton2
        takePhotoBtn.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(takePictureIntent, requestCodeTakePhoto)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeTakePhoto && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //photoImageView.setImageBitmap(imageBitmap)

            // Сохранение фотографии и обновление пути к файлу
            savePhoto(imageBitmap)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId)
        {
            102 -> {
                adapter.deleteItem(item.groupId, requireContext(), photosViewModel)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    private fun makeListOfImages(view: View)
    {
        recyclerView = binding.cameraImgRecView
        adapter = ImageAdapter()
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {

            photosViewModel.loadExpensePhotos(selectedItem)
        }
    }

    private fun savePhoto(bitmap: Bitmap) {

        try {
            val values = ContentValues()

            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image.jpg")
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder")

            var imageUri: Uri? = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            photosViewModel.addExpensePhoto(idOfExpenses.get(selectedItem)!!, imageUri!!)

            if (imageUri != null)
            {
                val fos: OutputStream? = requireActivity().contentResolver.openOutputStream(imageUri)

                if (fos != null)
                {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)

                }

                Toast.makeText(requireContext(), "Фото сохранено: ", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(requireContext(), "Фото не сохранилось(((", Toast.LENGTH_SHORT).show()
            }

            makeListOfImages(view1)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun loadExpenses()
    {
        var expenses = ArrayList<String>()
        var map: HashMap<String, Int> = HashMap()

        viewLifecycleOwner.lifecycleScope.launch {

            expenseViewModel.loadExpenses()

            expenseViewModel.expenses.collect {expensesList ->

                expenses.clear()
                map.clear()

                for (expense in expensesList)
                {
                    expenses.add( expense.expName )

                    map.put(expense.expName, expense.id!!)
                }

                listOfItems.clear()
                listOfItems.addAll(expenses)

                idOfExpenses.clear()
                idOfExpenses.putAll(map)

                Log.e("EXPENSES", listOfItems.toString())
                Log.e("MAP", idOfExpenses.toString())

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, expenses)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
    }
}