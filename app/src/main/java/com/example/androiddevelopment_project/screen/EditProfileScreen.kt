package com.example.androiddevelopment_project.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.androiddevelopment_project.utils.ImagePickerHelper
import com.example.androiddevelopment_project.viewmodel.UserProfileViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: UserProfileViewModel = koinViewModel()
) {
    val profileState by viewModel.editProfileState.collectAsState()
    val context = LocalContext.current
    val imagePickerHelper = remember { ImagePickerHelper(context) }
    
    var showImagePickerDialog by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    
    val hasPermissions = remember { ImagePickerHelper.hasPermissions(context) }
    var showPermissionDialog by remember { mutableStateOf(!hasPermissions) }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val permanentUri = imagePickerHelper.copyImageToAppStorage(it)
            permanentUri?.let { savedUri ->
                viewModel.updateAvatarUri(savedUri)
            }
        }
    }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempCameraUri?.let { uri ->
                viewModel.updateAvatarUri(uri)
            }
        }
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (!allGranted) {
            onNavigateBack()
        }
    }
    
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { 
                showPermissionDialog = false
                if (!ImagePickerHelper.hasPermissions(context)) {
                    onNavigateBack()
                }
            },
            title = { Text("Необходимы разрешения") },
            text = { Text("Для работы с фотографиями профиля необходим доступ к камере и хранилищу") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog = false
                        permissionLauncher.launch(ImagePickerHelper.REQUIRED_PERMISSIONS)
                    }
                ) {
                    Text("Предоставить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
    
    if (showImagePickerDialog) {
        AlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            title = { Text("Выберите источник") },
            text = { Text("Откуда вы хотите взять фотографию?") },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = {
                            showImagePickerDialog = false
                            galleryLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        Text("Галерея")
                    }
                    
                    TextButton(
                        onClick = {
                            showImagePickerDialog = false
                            val cameraIntent = imagePickerHelper.createCameraIntent()
                            if (cameraIntent != null) {
                                tempCameraUri = imagePickerHelper.getTempCameraUri()
                                if (tempCameraUri != null) {
                                    cameraLauncher.launch(tempCameraUri)
                                }
                            }
                        }
                    ) {
                        Text("Камера")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showImagePickerDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование профиля") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.saveProfile()
                            onNavigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Готово"
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { showImagePickerDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (profileState.avatarUri.isNotEmpty()) {
                        val uri = if (profileState.avatarUri.startsWith("file:")) {
                            val file = File(Uri.parse(profileState.avatarUri).path ?: "")
                            if (file.exists()) {
                                FileProvider.getUriForFile(
                                    context,
                                    "com.example.androiddevelopment_project.fileprovider",
                                    file
                                )
                            } else {
                                Uri.parse(profileState.avatarUri)
                            }
                        } else {
                            Uri.parse(profileState.avatarUri)
                        }
                        
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Аватар пользователя",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Добавить фото",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = profileState.fullName,
                    onValueChange = { viewModel.updateFullName(it) },
                    label = { Text("ФИО") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = profileState.position,
                    onValueChange = { viewModel.updatePosition(it) },
                    label = { Text("Должность") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = profileState.resumeUrl,
                    onValueChange = { viewModel.updateResumeUrl(it) },
                    label = { Text("Ссылка на резюме") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Например: https://example.com/resume.pdf") }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        viewModel.saveProfile()
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Готово")
                }
            }
        }
    }
} 