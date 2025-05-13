package com.example.androiddevelopment_project.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.androiddevelopment_project.R
import com.example.androiddevelopment_project.utils.ResumeDownloader
import com.example.androiddevelopment_project.viewmodel.UserProfileViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.File
import android.os.Build

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onEditClick: () -> Unit,
    viewModel: UserProfileViewModel = koinViewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val resumeDownloader = remember { ResumeDownloader(context) }
    
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: true
        if (writePermissionGranted && profile.resumeUrl.isNotEmpty()) {
            resumeDownloader.downloadResume(profile.resumeUrl)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Редактировать профиль"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if (profile.fullName.isEmpty() && profile.avatarUri.isEmpty()) {
                EmptyProfileView(onEditClick)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profile.avatarUri.isNotEmpty()) {
                            val uri = if (profile.avatarUri.startsWith("file:")) {
                                val file = File(Uri.parse(profile.avatarUri).path ?: "")
                                if (file.exists()) {
                                    FileProvider.getUriForFile(
                                        context,
                                        "com.example.androiddevelopment_project.fileprovider",
                                        file
                                    )
                                } else {
                                    Uri.parse(profile.avatarUri)
                                }
                            } else {
                                Uri.parse(profile.avatarUri)
                            }
                            
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Аватар пользователя",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = "Аватар пользователя",
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = profile.fullName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (profile.position.isNotEmpty()) {
                        Text(
                            text = profile.position,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (profile.resumeUrl.isNotEmpty()) {
                        Button(
                            onClick = {
                                // На Android 10+ (API 29+) не нужно разрешение WRITE_EXTERNAL_STORAGE для
                                // скачивания в публичную директорию Downloads
                                val hasWritePermission = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                                    ContextCompat.checkSelfPermission(
                                        context, 
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ) == PackageManager.PERMISSION_GRANTED
                                } else {
                                    true
                                }
                                
                                if (hasWritePermission) {
                                    resumeDownloader.downloadResume(profile.resumeUrl)
                                } else {
                                    // Запрашиваем только разрешение на запись, если оно необходимо
                                    requestPermissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                                }
                            },
                            modifier = Modifier.fillMaxWidth(0.7f)
                        ) {
                            Text("Скачать резюме")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyProfileView(onEditClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Профиль не заполнен",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Заполните информацию о себе, чтобы создать профиль",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Заполнить профиль")
                }
            }
        }
    }
} 