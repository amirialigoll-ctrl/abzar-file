package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.ToolItem
import com.example.model.availableTools
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolDetailScreen(
    toolId: String,
    onNavigateBack: () -> Unit
) {
    val tool = availableTools.find { it.id == toolId } ?: return
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tool.title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = tool.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (selectedFileUri == null) {
                Button(
                    onClick = { filePickerLauncher.launch(arrayOf("*/*")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("select_file_button")
                ) {
                    Text("Select File", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                Text(
                    text = "Selected File: ${selectedFileUri?.lastPathSegment}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                if (toolId == "compressor") {
                    var compressionLvl by remember { mutableFloatStateOf(5f) }
                    Text("Compression Level: ${compressionLvl.toInt()}")
                    Slider(
                        value = compressionLvl,
                        onValueChange = { compressionLvl = it },
                        valueRange = 1f..10f,
                        steps = 8,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isProcessing) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Processing...")
                } else if (resultMessage != null) {
                    Text(
                        text = resultMessage ?: "",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        selectedFileUri = null
                        resultMessage = null
                    }) {
                        Text("Process Another File")
                    }
                } else {
                    Button(
                        onClick = {
                            isProcessing = true
                            coroutineScope.launch {
                                // Simulate processing
                                delay(2000)
                                isProcessing = false
                                resultMessage = "File processed securely on-device and saved."
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("process_button")
                    ) {
                        Text("Start Action", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
