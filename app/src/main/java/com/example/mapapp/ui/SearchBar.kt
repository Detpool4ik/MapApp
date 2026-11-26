package com.example.mapapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(address: String, onAddressChange: (String) -> Unit, loading: Boolean, onSearch: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Введите адрес") },
            singleLine = true,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { if (!loading) onSearch() })
        )
        Button(onClick = onSearch, enabled = !loading) {
            if (loading) CircularProgressIndicator(
                modifier = Modifier.size(20.dp), strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            ) else Text("Найти")
        }
    }
}
