package com.illopen.agent.ui.fragments

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.illopen.agent.R
import com.illopen.agent.adapters.ProperyImagesListAdapter
import com.illopen.agent.databinding.FragmentPropertyImageBinding
import com.illopen.agent.model.PropertyImageList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.MediaLoader
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PropertyImageFragment : Fragment(R.layout.fragment_property_image) , ProperyImagesListAdapter.OnItemClickListener {

    private val TAG: String = "PropertyImageFragment"
    private var _binding: FragmentPropertyImageBinding? = null
    private val binding get() = _binding!!

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    lateinit var mDialog : Dialog

    private var propertyMasterId : Int = 0

    private var filePath = ""

    private lateinit var progressDialog: ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPropertyImageBinding.bind(view)

        progressDialog = ProgressDialog(requireContext())

        Album.initialize(
            AlbumConfig.newBuilder(requireContext())
                .setAlbumLoader(MediaLoader())
                .build()
        )

        propertyMasterId = activity?.intent!!.getIntExtra("PropertyMasterId",0)

        binding.propertyImage.setOnClickListener {
                setupPermissions()
        }

        propertyImageAPI()
    }

    private fun propertyImageAPI() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.propertyImageAll(
                    propertyMasterId,map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getAllProperty", response.code().toString())
                        Log.d("getAllProperty",response.body().toString())

                        if (response.code() == 200) {
                            val list: List<PropertyImageList> = response.body()!!.values!!
                            binding.imageList.adapter = ProperyImagesListAdapter(
                                requireActivity(),
                                list,
                                this@PropertyImageFragment
                            )
                        }else{

                        }
                    }
                }else{
                    withContext(Dispatchers.Main){
                        Log.d(TAG, "something wrong")
                    }
                }
            }catch (e : Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }

    private fun setupPermissions() {

        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Storage Permission Denied")
            makeRequest()
        } else {
            openGallery()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }

    private fun openGallery() {
        Album.image(this)
            .multipleChoice()
            .camera(false)
            .columnCount(4)
            .selectCount(1)
            .onResult {
                filePath = it[0].path
                confirmImageOrder(filePath)
//                Glide.with(this)
//                    .load(filePath)
//                    .into(binding.propertyImage)
                Log.d(TAG, "openGallery: ${filePath}")
                Log.d(TAG, "openGallery: ${Gson().toJson(it)}")
            }
            .onCancel {
                Log.d(TAG, "openGallery: $it")
            }
            .start()
    }

    private fun confirmImageOrder(filePath: String) {
        progressDialog.dialog.show()
        val imageToBeUploaded = File(filePath)
        coroutineScope.launch {
            try {

                val requestFile = MultipartBody.Part.createFormData("Files", "${System.currentTimeMillis()}.png",
                    File(filePath).asRequestBody("multipart/form-data".toMediaTypeOrNull()))

                val response = ServiceApi.retrofitService.propertyImageUpload(propertyMasterId,requestFile)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d(TAG, "propertyImage: ${response.code()}")
                        Log.d(TAG, "propertyImage:${response.body()}")

                        if (response.code() == 200){
                            propertyImageAPI()
                            requireActivity().toast("Successful Image Upload")
                        }else{

                        }
                        progressDialog.dialog.dismiss()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "error: ${response.code()}")
                        Log.d(TAG, "error: ${response.body()}")
                        progressDialog.dialog.dismiss()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "propertyImage: ${e.message}")
                    progressDialog.dialog.dismiss()
                }
            }
        }
    }

    override fun onItemDeleteClick(itemPosition: Int, deleteImage: PropertyImageList) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle("DELETE")
        dialog.setMessage("Are You Sure You Want to Delete?")

        dialog.setPositiveButton("YES") { dialog: DialogInterface, i: Int ->
            deletePropertyImage(deleteImage)
        }

        dialog.setNegativeButton("NO") { dialog: DialogInterface, i: Int ->
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deletePropertyImage(deleteImage: PropertyImageList) {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.propertyImageDelete(
                    deleteImage.id!!.toInt()
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("PropertyImageDeleted", response.code().toString())
                        Log.d("PropertyImageDeleted", response.body().toString())

                        if (response.code() == 200){
                            propertyImageAPI()
                            requireActivity().toast("Delete Image Successfully")
                        }else{
                            requireActivity().toast("please try again..")
                        }

                        progressDialog.dialog.dismiss()

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                        progressDialog.dialog.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                progressDialog.dialog.dismiss()
            }
        }
    }
}