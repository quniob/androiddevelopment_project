package com.example.androiddevelopment_project.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast

class ResumeDownloader(private val context: Context) {
    
    companion object {
        private const val TAG = "ResumeDownloader"
    }

    fun downloadResume(resumeUrl: String) {
        try {
            Log.d(TAG, "Начинаем скачивание резюме: $resumeUrl")
            val fileName = getFileNameFromUrl(resumeUrl)
            val uri = Uri.parse(resumeUrl)
            
            val request = DownloadManager.Request(uri)
                .setTitle("Скачивание резюме")
                .setDescription("Скачивание файла $fileName")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            
            Toast.makeText(context, "Скачивание начато. Проверьте панель уведомлений.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при скачивании: ${e.message}", e)
            Toast.makeText(context, "Ошибка при скачивании: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileNameFromUrl(url: String): String {
        try {
            return url.substring(url.lastIndexOf('/') + 1)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении имени файла из URL: $url", e)
            return "resume_${System.currentTimeMillis()}.pdf"
        }
    }
} 