package com.github.presentation.features.users.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.github.domain.model.GitHubUserDetails
import com.github.presentation.ui.components.ErrorView
import com.github.presentation.ui.components.FavouriteTopBar
import com.github.presentation.ui.components.ProgressBar
import com.github.presentation.features.users.details.GitHubUserDetailsContract.Event
import com.github.presentation.features.users.details.GitHubUserDetailsContract.State
import com.github.presentation.model.GitHubUserUi
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun GitHubUserDetailsScreen(
    viewModel: GitHubUserDetailsViewModel,
    gitHubUser: GitHubUserUi,
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        (viewModel::sendViewEvent)(Event.GetGitHubUserDetailsAction(gitHubUser))
    }
    GitHubUserDetailsContent(
        uiState,
        onFavouriteToggle = { (viewModel::sendViewEvent)(Event.ToggleFavouriteGitHubUserAction) },
        onBackPressed
    )
}

@Composable
private fun GitHubUserDetailsContent(
    uiState: State,
    onFavouriteToggle: () -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            FavouriteTopBar(
                isFavourite = uiState.userDetails?.isFavourite ?: false,
                onFavouriteToggle,
                onBackPressed
            )
        }
    ) {
        Box(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            when {
                uiState.isLoading -> ProgressBar()
                uiState.error != null -> ErrorView(uiState.error.message ?: "Unknown Error")
                uiState.userDetails != null -> UserDetailsContent(uiState.userDetails)
            }
        }
    }
}

@Composable
private fun UserDetailsContent(userDetails: GitHubUserDetails) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(userDetails.avatarUrl)
            .size(Size.ORIGINAL)
            .memoryCacheKey(userDetails.id.toString())
            .build()
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .size(164.dp)
                .clip(CircleShape)
                .placeholder(
                    visible = painter.state is AsyncImagePainter.State.Loading,
                    color = Color.Gray,
                    highlight = PlaceholderHighlight.shimmer()
                ),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = userDetails.login,
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = userDetails.name ?: "Unknown name",
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = userDetails.location ?: "Unknown location",
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
