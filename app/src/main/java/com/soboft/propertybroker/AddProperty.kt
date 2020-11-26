package com.soboft.propertybroker

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.soboft.propertybroker.adapters.AmenitiesAdapter
import com.soboft.propertybroker.databinding.ActivityAddPropertyBinding
import com.soboft.propertybroker.model.AmenitiesModel
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class AddProperty : AppCompatActivity() {
    private lateinit var binding: ActivityAddPropertyBinding
    val REQUEST_CODE_SELECT_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }

        binding.addImageFromGallery.setOnClickListener {
            selectImage()
        }

        binding.selectTime.setOnClickListener {
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

//            binding.selectTime.text =

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

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    @Suppress("DEPRECATION")
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
                        binding.addImage.setImageBitmap(bitmap)
                        binding.plusBtn.visibility = View.GONE
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}