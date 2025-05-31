package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.restaurantmanagementapp.R


@Composable
fun AddEditCategoryDialog(
    initialName: String = "",
    initialImageRes: Int? = null,
    onConfirm: (String, Int) -> Unit,
    onDismiss: () -> Unit,
    availableImages: List<Int> // categoryImageResList
) {
    var name by remember { mutableStateOf(initialName) }
    var selectedImageRes by remember { mutableStateOf(initialImageRes ?: availableImages.firstOrNull() ?: 0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(if (initialName.isEmpty()) "Add Category" else "Edit Category") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Select Image:")
                Row (
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                ) {
                    availableImages.forEach { resId ->
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp)
                                .clickable { selectedImageRes = resId }
                                .then(
                                    if (selectedImageRes == resId) Modifier.border(2.dp, MaterialTheme.colorScheme.primary)
                                    else Modifier
                                )
                        )
                    }
                }

                }
            },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, selectedImageRes) },
                enabled = name.isNotBlank()
            ) { Text("Save") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun PreviewAddEditCategoryDialog() {
    // Provided some mock drawable resource IDs
    val mockImages = listOf(
        R.drawable.cat1,
        R.drawable.cat2,
        R.drawable.cat3
    )
    MaterialTheme {
        AddEditCategoryDialog(
            initialName = "Pizza",
            initialImageRes = R.drawable.cat1,
            onConfirm = { _, _ -> },
            onDismiss = {},
            availableImages = mockImages
        )
    }
}