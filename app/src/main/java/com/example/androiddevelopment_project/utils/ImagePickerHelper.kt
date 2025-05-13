package com.example.androiddevelopment_project.utils

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Environment

class ImagePickerHelper(private val context: Context) {
    
    private var tempCameraUri: Uri? = null
    
    companion object {
        const val FILE_PROVIDER_AUTHORITY = "com.example.androiddevelopment_project.fileprovider"
        private const val PERMISSION_REQUEST_CODE = 100
        
        val REQUIRED_PERMISSIONS =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )

        fun hasPermissions(context: Context): Boolean {
            return REQUIRED_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        }
        
        fun requestPermissions(activity: Activity) {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
        }
    }

    fun createGalleryIntent(): Intent {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        return intent
    }

    fun createCameraIntent(): Intent? {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            try {
                val imageFile = createImageFile()
                if (imageFile != null) {
                    tempCameraUri = FileProvider.getUriForFile(
                        context,
                        FILE_PROVIDER_AUTHORITY,
                        imageFile
                    )
                    
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, tempCameraUri)
                    
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    return intent
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getTempCameraUri(): Uri? {
        return tempCameraUri
    }

    private fun createImageUri(): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, createImageFileName())
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/AndroidMovieApp")
            }
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            val imageFile = createImageFile()
            imageFile?.let {
                FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, it)
            }
        }
    }

    private fun createImageFile(): File? {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "CAMERA_${timeStamp}"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (!storageDir?.exists()!!) {
                storageDir.mkdirs()
            }
            return File.createTempFile(fileName, ".jpg", storageDir)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    

    private fun createImageFileName(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "IMG_${timeStamp}.jpg"
    }

    fun copyImageToAppStorage(sourceUri: Uri): Uri? {
        try {
            val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return null
            val fileName = "profile_avatar_${System.currentTimeMillis()}.jpg"
            val outputFile = File(context.filesDir, fileName)
            
            inputStream.use { input ->
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            
            return Uri.fromFile(outputFile)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
} 