package com.example.restaurantmanagementapp.Activity.CustomerActivity

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class CartItem(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    var quantity: Int
)

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    onBack: () -> Unit,
    onEdit: (CartItem) -> Unit,
    onRemove: (CartItem) -> Unit,
    onQuantityChange: (CartItem, Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf<String?>(null) }
    var showOrderReceived by remember { mutableStateOf(false) }
    var showReceiptSent by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // Payment methods
    val paymentMethods = listOf("Credit Card", "PayPal", "Google Pay")

    // Show "order received" for 4 seconds, then show "receipt sent"
    fun handleOrder() {
        showCheckoutDialog = false
        showOrderReceived = true
        snackbarMessage = "Order received"
        scope.launch {
            delay(4000)
            showOrderReceived = false
            showReceiptSent = true
            snackbarMessage = "Receipt sent"
            delay(3000)
            showReceiptSent = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF23262F))
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Basket",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Order List
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                cartItems.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDAD1D1))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "$${"%.2f".format(item.price * item.quantity)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF23262F)
                                )
                            }
                            item.description?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = it,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Quantity controls
                                OutlinedButton(
                                    onClick = { if (item.quantity > 1) onQuantityChange(item, item.quantity - 1) },
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("-", fontSize = 18.sp)
                                }
                                Text(
                                    text = item.quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    fontSize = 16.sp
                                )
                                OutlinedButton(
                                    onClick = { onQuantityChange(item, item.quantity + 1) },
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Text("+", fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                IconButton(onClick = { onEdit(item) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }
                                IconButton(onClick = { onRemove(item) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                                }
                            }
                        }
                    }
                }
            }

            // Go to checkout button
            Button(
                onClick = { showCheckoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xF7E03F3A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Go to Checkout", fontSize = 18.sp)
            }
        }

        // Checkout Dialog
        if (showCheckoutDialog) {
            AlertDialog(
                onDismissRequest = { showCheckoutDialog = false },
                title = { Text("Select Payment Method") },
                text = {
                    Column {
                        paymentMethods.forEach { method ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { selectedPayment = method }
                            ) {
                                RadioButton(
                                    selected = selectedPayment == method,
                                    onClick = { selectedPayment = method }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(method, fontSize = 16.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (selectedPayment != null) handleOrder()
                        },
                        enabled = selectedPayment != null
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCheckoutDialog = false }) {
                        Text("Cancel")
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }

        // Snackbar for notifications (bottom)
        if (showOrderReceived) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = Color(0xFF23262F)
            ) {
                Text(snackbarMessage, color = Color.White)
            }
        }

        // Top notification for "receipt sent"
        if (showReceiptSent) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
            ) {
                Surface(
                    color = Color(0xFF23262F),
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 6.dp
                ) {
                    Text(
                        text = snackbarMessage,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

// Example usage (replace with your own ViewModel and state management)
@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    var cartItems by remember {
        mutableStateOf(
            listOf(
                CartItem(1, "Burger", "Juicy beef burger", 7.99, 2),
                CartItem(2, "Fries", null, 2.99, 1),
                CartItem(3, "Coke", "Chilled soft drink", 1.99, 3)
            )
        )
    }
    CartScreen(
        cartItems = cartItems,
        onBack = {},
        onEdit = { /* Show edit dialog */ },
        onRemove = { item -> cartItems = cartItems.filter { it.id != item.id } },
        onQuantityChange = { item, qty ->
            cartItems = cartItems.map {
                if (it.id == item.id) it.copy(quantity = qty) else it
            }
        }
    )
}