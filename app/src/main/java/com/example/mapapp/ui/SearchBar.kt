package com.example.mapapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.mapapp.R

@Composable
fun SearchBar(address: String, onAddressChange: (String) -> Unit, loading: Boolean, onSearch: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.search_spacing))) {
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text(stringResource(R.string.address_hint)) },
            singleLine = true,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { if (!loading) onSearch() })
        )
        Button(onClick = onSearch, enabled = !loading) {
            if (loading) CircularProgressIndicator(
                modifier = Modifier.size(dimensionResource(R.dimen.progress_size)),
                strokeWidth = dimensionResource(R.dimen.progress_stroke),
                color = MaterialTheme.colorScheme.primary
            ) else Text(stringResource(R.string.search))
        }
    }
}
