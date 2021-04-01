package com.illopen.agent.ui.activities

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.illopen.agent.R
import com.illopen.agent.adapters.DocumentsAdapter
import com.illopen.agent.databinding.ActivityDocumentsBinding
import com.illopen.agent.model.*
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import com.yanzhenjie.album.Album
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.FilePickerConst.KEY_SELECTED_DOCS
import droidninja.filepicker.models.sort.SortingTypes
import droidninja.filepicker.utils.ContentUriUtils
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class Documents : AppCompatActivity(), DocumentsAdapter.OnItemClickListener {

    private lateinit var binding: ActivityDocumentsBinding

    private val TAG = "User Documents"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    val RC_PHOTO_PICKER_PERM = 123
    val RC_FILE_PICKER_PERM = 321
    private val CUSTOM_REQUEST_CODE = 532
    private val MAX_ATTACHMENT_COUNT = 10
    private val photoPaths = ArrayList<Uri>()
    private var docPaths = ArrayList<Uri>()
    private var filePath = ""


    private var userDocumentList: ArrayList<DocumentTypeList> = ArrayList()
    private lateinit var userDropDownAdapter: ArrayAdapter<String>
    private var userTypeMasterId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener { onBackPressed() }

        getAllUserDocument()

        selectDocTypeSpinner()

        binding.image.setOnClickListener {
            picImage()
        }
        binding.pdf.setOnClickListener {
            chooseDocument()
        }
    }

    private fun createUserDocument() {
        coroutineScope.launch {
            try {

                val documentRequest = MultipartBody.Part.createFormData(
                    "files", "${System.currentTimeMillis()}" +
                            ".${
                                MimeTypeMap.getFileExtensionFromUrl(
                                    ContentUriUtils.getFilePath(
                                        this@Documents,
                                        docPaths[0]
                                    ).toString()
                                )
                            }",
                    File(
                        ContentUriUtils.getFilePath(
                            this@Documents,
                            docPaths[0]
                        )!!
                    ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )

                val response = ServiceApi.retrofitService.uploadDocument(
                    AppPreferences.getUserData(Params.UserId).toInt(),
                    userTypeMasterId,
                    documentRequest
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "UserDocumentUpload: ${response.code()}")
                        Log.d(TAG, "UserDocumentUpload:${response.body()}")

                        toast("Successfully Document Uploaded")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, e.message.toString())
                }
            }
        }
    }

    private fun picImage() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission to record denied")
            makeRequest()
        } else {
            openImage()
        }
    }

    private fun openImage() {

        Album.image(this)
            .multipleChoice()
            .camera(false)
            .columnCount(4)
            .selectCount(1)
            .onResult {
                filePath = it[0].path
                confirmImageOrder(filePath)
                Glide.with(this)
                    .load(filePath)
//                    .into(binding.profileImage)
                Log.d(TAG, "openGallery: $filePath")
                Log.d(TAG, "openGallery: ${Gson().toJson(it)}")
            }
            .onCancel {
                Log.d(TAG, "openGallery: $it")
            }
            .start()

//        val maxCount = MAX_ATTACHMENT_COUNT - docPaths.size
//        if (docPaths.size + photoPaths.size == MAX_ATTACHMENT_COUNT) {
//            Toast.makeText(
//                this, "Cannot select more than $MAX_ATTACHMENT_COUNT items",
//                Toast.LENGTH_SHORT
//            ).show()
//        } else {
//            FilePickerBuilder.instance
//                .setMaxCount(1)
//                .setSelectedFiles(photoPaths)
//                .setActivityTheme(R.style.LibAppTheme)
//                .setActivityTitle("Please select media")
////                .enableVideoPicker(true)
//                .enableCameraSupport(true)
//                .showGifs(true)
//                .showFolderView(true)
//                .enableSelectAll(false)
//                .enableImagePicker(true)
//                .setCameraPlaceholder(R.drawable.album_ic_image_camera_white)
//                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .pickPhoto(this, CUSTOM_REQUEST_CODE)
//        }

    }

    private fun confirmImageOrder(filePath: String?) {

        coroutineScope.launch {
            try {

                val imageRequest = MultipartBody.Part.createFormData(
                    "files", "${System.currentTimeMillis()}.jpg",
                    File(filePath!!).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )

//                val  documentRequest = MultipartBody.Part.createFormData(
//                    "files","${System.currentTimeMillis()}" +
//                            ".${MimeTypeMap.getFileExtensionFromUrl(ContentUriUtils.getFilePath(this@Documents, docPaths[0]).toString())}",
//                    File(ContentUriUtils.getFilePath(this@Documents, docPaths[0])!!).asRequestBody("multipart/form-data".toMediaTypeOrNull()))

                val response = ServiceApi.retrofitService.uploadDocument(
                    AppPreferences.getUserData(Params.UserId).toInt(),
                    userTypeMasterId,
                    imageRequest,
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "imageUploadSuccess: ${response.code()}")
                        Log.d(TAG, "imageUploadSuccess:${response.body()}")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "error: ${response.code()}")
                        Log.d(TAG, "error: ${response.body()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, e.message.toString())
                }
            }
        }
    }

    private fun selectDocTypeSpinner() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getDocumentType(
                    AppPreferences.getUserData(Params.UserId).toInt(), map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("selectDocTypeSpinner", response.code().toString())
                        Log.d("selectDocTypeSpinner", response.body().toString())

                        userDocumentList = response.body()?.values as ArrayList<DocumentTypeList>

                        val data: MutableList<String> = ArrayList()

                        userDocumentList.forEach {
                            data.add(it.name.toString())
                        }

                        userDropDownAdapter = object : ArrayAdapter<String>(
                            this@Documents,
                            android.R.layout.simple_list_item_1, data
                        ) {}
                        binding.docTypeSpinner.adapter = userDropDownAdapter

                        binding.docTypeSpinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {

                                override fun onItemSelected(
                                    parent: AdapterView<*>?, view: View?,
                                    position: Int, id: Long
                                ) {
                                    userTypeMasterId = userDocumentList[position].id!!.toInt()
                                    toast("Selected : " + userDocumentList[position].name)
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    //nothing select code
                                }
                            }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    private fun chooseDocument() {

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission to record denied")
            makeRequest()
        } else {
            openPdfChooser()
        }
    }

    private fun openPdfChooser() {
        val zips = arrayOf("zip", "rar")
        val pdfs = arrayOf("aac")
        val maxCount: Int = MAX_ATTACHMENT_COUNT - photoPaths.size
        if (docPaths.size + photoPaths.size == MAX_ATTACHMENT_COUNT) {
            Toast.makeText(
                this, "Cannot select more than $MAX_ATTACHMENT_COUNT items",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            FilePickerBuilder.instance
                .setMaxCount(1)
                .setSelectedFiles(docPaths)
                .setActivityTheme(R.style.LibAppTheme)
                .setActivityTitle("Please select doc")
                .setImageSizeLimit(5) //Provide Size in MB
                .setVideoSizeLimit(20) //                    .addFileSupport("ZIP", zips)
                //                    .addFileSupport("AAC", pdfs, R.drawable.pdf_blue)
                .enableDocSupport(true)
                .enableSelectAll(true)
                .sortDocumentsBy(SortingTypes.NAME)
                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .pickFile(this)
            createUserDocument()
        }

        /*val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "application/pdf"
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE)*/

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun showSettingsDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage(
            "This app needs permission to use this feature. You can grant them in app settings."
        )
        builder.setPositiveButton("GOTO SETTINGS") { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings()
        }
        val negativeButton = builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_DOC -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val dataList = data.getParcelableArrayListExtra<Uri>(KEY_SELECTED_DOCS)
                    if (dataList != null) {
//                       val fileUriProfile = data.data
                        docPaths = java.util.ArrayList()
                        docPaths.addAll(dataList)
//                        binding.pdf.text = ContentUriUtils.getFilePath(this, docPaths[0])
                    }
                }
            }
        }
    }

    private fun getAllUserDocument() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getUserAllDocuments(
                    AppPreferences.getUserData(Params.UserId).toInt(),
                    map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getAllDocument", response.code().toString())
                        Log.d("getAllDocument", response.body().toString())

                        val list: List<UserDocumentList> = response.body()!!.values!!

                        binding.documentRv.adapter = DocumentsAdapter(
                            this@Documents,
                            list, this@Documents
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    override fun onDocumentClick(itemPosition: Int, document: UserDocumentList) {
        //todo here delete id and delete by same id's so issue in api side//
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("DELETE")
        dialog.setMessage("Are You Sure You Want to Delete?")

        dialog.setPositiveButton("YES") { dialog: DialogInterface, i: Int ->
            deleteDoc(document)
        }

        dialog.setNegativeButton("NO") { dialog: DialogInterface, i: Int ->
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteDoc(document: UserDocumentList) {
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.deleteUserDoc(
                    document.id!!.toInt(),
                    document.userId!!.toInt()
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("deleteDocument", response.code().toString())
                        Log.d("deleteDocument", response.body().toString())

                        toast("Delete Document Successfully")
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

}