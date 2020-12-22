package com.soboft.propertybroker.ui.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.AllPropertyListAdapter
import com.soboft.propertybroker.adapters.AmenitiesAdapter
import com.soboft.propertybroker.adapters.ProperyImagesListAdapter
import com.soboft.propertybroker.databinding.ActivityAddPropertyBinding
import com.soboft.propertybroker.databinding.ActivityNewJobBinding
import com.soboft.propertybroker.model.AmenitiesModel
import com.soboft.propertybroker.model.PropertyImageModel
import com.soboft.propertybroker.model.PropertyListModel
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class AddNewJob : AppCompatActivity() {

    private lateinit var binding: ActivityNewJobBinding
    val REQUEST_CODE_SELECT_IMAGE = 1

    internal var dropDownValue = arrayOf("Per Month", "Per Year")
    var selectedDropDown: String = "Per Month"
    private lateinit var dropDownAdapter: ArrayAdapter<String>

    internal var propertyListValue = arrayOf("Shivalik Shilp", "Aditya Prime", "Saujanya 2")
    private lateinit var propertyListAdapterSpinner: ArrayAdapter<String>

    private var imageList: ArrayList<Uri>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }

        binding.addImageFromGallery.setOnClickListener {
            /*FishBun.with(this).setImageAdapter(GlideAdapter())
                .startAlbum()*/

            selectImage()
        }

        binding.saleRentRG.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.sale -> {
                    binding.spinner.visibility = View.GONE
                }
                R.id.rent -> {
                    binding.spinner.visibility = View.VISIBLE
                }
            }
        }

        dropDownAdapter = object :
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dropDownValue) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                (v as TextView).textSize = 12.0F
                return v
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getDropDownView(position, convertView, parent)
                (v as TextView).textSize = 12.0F
                return v
            }
        }
        binding.monthYearSpinner.adapter = dropDownAdapter

        propertyListAdapterSpinner = object :
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, propertyListValue) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                (v as TextView).textSize = 12.0F
                return v
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getDropDownView(position, convertView, parent)
                (v as TextView).textSize = 12.0F
                return v
            }
        }
        binding.propertySpinner.adapter = propertyListAdapterSpinner


        binding.selectTime.setOnClickListener {
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.show(supportFragmentManager, "fragment_tag")
        }

        binding.selectDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            val now = Calendar.getInstance()
            builder.setSelection(now.timeInMillis)
            picker.show(supportFragmentManager, picker.toString())
        }

        binding.addAmenitites.setOnClickListener {
            val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
            mDialog.setContentView(R.layout.amenitied_list_popup)

            val back = mDialog.findViewById<ImageView>(R.id.back)

            val list = ArrayList<AmenitiesModel>()
            list.add(AmenitiesModel("Kitchen"))
            list.add(AmenitiesModel("Garden"))
            list.add(AmenitiesModel("CCTV"))
            list.add(AmenitiesModel("School Bus Area"))
            list.add(AmenitiesModel("Walking Area"))
            val amenitiesRv = mDialog.findViewById<RecyclerView>(R.id.amenitiesRv)
            amenitiesRv.adapter = AmenitiesAdapter(list)

            back.setOnClickListener {
                mDialog.dismiss()
            }

            mDialog.show()
        }

    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FishBun.FISHBUN_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    imageList = data?.getParcelableArrayListExtra(FishBun.INTENT_PATH)
                    setImages()
                }
            }
        }
    }
*/
   /* private fun setImages() {
        if (imageList != null) {
            val list = ArrayList<PropertyImageModel>()
            for (it in imageList!!.indices) {
                list.add(PropertyImageModel(it.toString()))
            }
            binding.propertyImageRv.adapter = ProperyImagesListAdapter(list)
        }
    }
*/
    private fun selectImage() {
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val selectImageUri: Uri? = data.getData()
                binding.addImage.setImageBitmap(
                    MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(),
                        selectImageUri
                    )
                )
                if (selectImageUri != null) {
                    try {
                        val inputStream: InputStream? =
                            contentResolver.openInputStream(selectImageUri)
                        val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
//                        binding.addImage.setImageBitmap(bitmap)
//                        binding.plusBtn.visibility = View.GONE
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}