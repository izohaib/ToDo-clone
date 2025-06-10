package com.example.todo.presentation.update.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FilePickerSectionj(
    fileUris: List<Uri>,
    onAddFile: (Uri) -> Unit,
    onRemoveFile: (Uri) -> Unit
) {


    val context = LocalContext.current
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { onAddFile(it) }
    }

    Column(modifier = Modifier.padding(8.dp)) {

        Text("Attached Files", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        fileUris.forEach { uri ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Open file when row is clicked
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, context.contentResolver.getType(uri))
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }
                        context.startActivity(intent)
                    }
                    .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.InsertDriveFile,
                    contentDescription = null,
                    tint = Color.Blue
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = uri.lastPathSegment ?: "File",
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(onClick = { onRemoveFile(uri) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove File"
                    )
                }
            }

            Spacer(Modifier.height(6.dp))
        }

        // Add file row
        OutlinedButton(
            onClick = { fileLauncher.launch(arrayOf("*/*")) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AttachFile, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Attach File")
        }
    }
}
@Composable
fun FilePickerSection(
    fileUris: List<Uri>,
    onAddFile: (Uri) -> Unit,
    onRemoveFile: (Uri) -> Unit
) {
    val context = LocalContext.current

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            // Persist URI permission to keep access after app restarts
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace() // Permission might already be taken or failed
            }

            onAddFile(it)
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {

        fileUris.forEach { uri ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, context.contentResolver.getType(uri))
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Unable to open file", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.InsertDriveFile,
                    contentDescription = null,
                    tint = Color.Blue
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = uri.lastPathSegment ?: "File",
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(onClick = { onRemoveFile(uri) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove File"
                    )
                }
            }

            Spacer(Modifier.height(6.dp))
        }

        OutlinedButton(
            onClick = { fileLauncher.launch(arrayOf("*/*")) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AttachFile, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Attach File")
        }
    }
}