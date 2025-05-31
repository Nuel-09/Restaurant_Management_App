package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun FoodAddEditDialog(
    show: Boolean,
    initialName: String = "",
    initialDescription: String = "",
    initialImagePath: String = "",
    initialPrice: String = "",
    initialTimeValue: String = "",
    initialCalorie: String = "",
    availableImages: List<Int>,
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        description: String,
        imagePath: String,
        price: Double,
        timeValue: Int,
        calorie: Int,
        id: Int?
    ) -> Unit,
    editingFoodId: Int? = null
) {
    if (!show) return

    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }
    var price by remember { mutableStateOf(initialPrice) }
    var timeValue by remember { mutableStateOf(initialTimeValue) }
    var calorie by remember { mutableStateOf(initialCalorie) }
    var selectedImage by remember { mutableStateOf(initialImagePath) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val priceValue = price.toDoubleOrNull() ?: 0.0
                    val timeValueInt = timeValue.toIntOrNull() ?: 0
                    val calorieInt = calorie.toIntOrNull() ?: 0
                    onConfirm(name, description, selectedImage, priceValue, timeValueInt, calorieInt, editingFoodId)
                },
                enabled = name.isNotBlank() && price.toDoubleOrNull() != null && selectedImage.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(if (initialName.isBlank()) "Add Food Item" else "Edit Food Item") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 400.dp, max = 600.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = timeValue,
                    onValueChange = { timeValue = it },
                    label = { Text("Time Value (minutes)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = calorie,
                    onValueChange = { calorie = it },
                    label = { Text("Calorie") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Select Food Image", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(80.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    items(availableImages) { imageRes ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(72.dp)
                                .background(
                                    if (selectedImage == imageRes.toString()) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else Color.Transparent
                                )
                                .clickable { selectedImage = imageRes.toString() },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}
