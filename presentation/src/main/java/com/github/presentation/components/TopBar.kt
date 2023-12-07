@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.github.presentation.R

@Composable
fun FilterTopBar(
    isFilterActive: Boolean,
    onFilterClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Користувачі GitHub")
        },
        navigationIcon = {},
        actions = {
            IconButton(onClick = onFilterClick) {
                Icon(
                    painterResource(id = R.drawable.ic_filter),
                    contentDescription = "",
                    tint = if (isFilterActive) {
                        Color(red = 253, green = 191, blue = 3, alpha = 255)
                    } else {
                        Color.Black
                    }
                )
            }
        }
    )
}

@Composable
fun FavouriteTopBar(
    isFavourite: Boolean,
    onFavouriteToggle: () -> Unit,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Деталі користувача")
        },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onFavouriteToggle) {
                Icon(
                    painterResource(id = R.drawable.ic_favorite),
                    contentDescription = "",
                    tint = if (isFavourite) {
                        Color(red = 253, green = 191, blue = 3, alpha = 255)
                    } else {
                        Color.Black
                    }
                )
            }
        }
    )
}