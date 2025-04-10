package com.example.lr4_second.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.R
import com.example.lr4_second.adapter.ImageAdapter
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.db.Photos
import com.example.lr4_second.model.ImageModel
import java.io.File
import java.io.OutputStream

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        spinner = view.findViewById<Spinner>(R.id.cameraSpinner)
        listOfItems = ArrayList()
        idOfExpenses = HashMap()
        imageList = ArrayList()

        loadExpenses()
        selectedItem = ""

        view1 = view

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = listOfItems.get(position)
                makeListOfImages(view1)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }


        var takePhotoBtn = view.findViewById<Button>(R.id.cameraButton2)
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
                adapter.deleteItem(item.groupId, requireContext())
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    private fun makeListOfImages(view: View)
    {
        val db = MainDB.getDB(requireContext())
        db.getDao().getExpensePhotos(selectedItem).asLiveData().observe(this)
        { itList ->
            imageList = ArrayList()
            itList.forEach{
                var image = ImageModel(it.id, it.imageUri)
                imageList.add(image)
            }

            initial(view, imageList)
        }
    }

    private fun initial(view: View, list: ArrayList<ImageModel>)
    {
        recyclerView = view.findViewById(R.id.cameraImgRecView)
        adapter = ImageAdapter()
        recyclerView.adapter = adapter

        adapter.setList(list)
    }

    private fun savePhoto(bitmap: Bitmap) {

        try {
            val values = ContentValues()

            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image.jpg")
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder")

            var imageUri: Uri? = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            //Сохранение в базу данных
            val db = MainDB.getDB(requireContext())
            var photo = Photos(null, idOfExpenses.get(selectedItem)!!, imageUri.toString())
            Thread{
                //db.getDao().updateExpenseImage(selectedItem, imageUri.toString())
                db.getDao().insertPhoto(photo)
            }.start()

            /*
            val sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putString(SAVED_IMAGE_URI_KEY, imageUri.toString())
            editor.apply()

             */

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

        val db = MainDB.getDB(requireContext())
        db.getDao().getAllItems().asLiveData().observe(viewLifecycleOwner)
        { itList ->
            expenses = ArrayList()
            var map: HashMap<String, Int> = HashMap()
            itList.forEach{
                expenses.add(it.expenseName)
                map.put(it.expenseName, it.id!!)
            }

            listOfItems = expenses
            idOfExpenses = map

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, expenses)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}