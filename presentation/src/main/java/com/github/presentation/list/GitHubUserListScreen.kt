@file:OptIn(ExperimentalFoundationApi::class)

package com.github.presentation.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.github.domain.model.GitHubUser
import com.github.presentation.R
import com.github.presentation.components.ErrorView
import com.github.presentation.components.ProgressBar
import com.github.presentation.components.FilterTopBar
import com.github.presentation.list.GitHubUserListContract.Event
import com.github.presentation.list.GitHubUserListContract.State
import com.github.presentation.list.GitHubUserListContract.Effect
import com.github.presentation.utils.rememberForeverLazyListState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun GitHubUserListScreen(
    viewModel: GitHubUserListViewModel,
    onUserClick: (GitHubUser) -> Unit,
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.sendViewEvent(Event.GetGitHubUsersAction)
    }
    viewModel.viewEffect.collectAsStateWithLifecycle(null).value?.let {
        LaunchedEffect(Unit) {
            when (it) {
                is Effect.ShowGitHubUserDetailsScreen -> onUserClick(it.user)
            }
        }
    }
    GitHubUserListContent(
        uiState,
        onFilterClick = { viewModel.sendViewEvent(Event.ToggleFilterAction) },
        onUserClick = { viewModel.sendViewEvent(Event.GitHubUserClickAction(it)) },
        onFavouriteUserClick = { viewModel.sendViewEvent(Event.ToggleFavouriteGitHubUserAction(it))}
    )
}

@Composable
private fun GitHubUserListContent(
    uiState: State,
    onFilterClick: () -> Unit,
    onUserClick: (GitHubUser) -> Unit,
    onFavouriteUserClick: (GitHubUser) -> Unit
) {
    Scaffold(
        topBar = {
            FilterTopBar(
                uiState.isFilterActive,
                onFilterClick
            )
        }
    ) {
        Box(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            when {
                uiState.isLoading -> ProgressBar()
                uiState.error != null -> ErrorView(uiState.error.message ?: "Unknown Error")
                else -> {
                    if (uiState.isFilterActive) {
                        GroupedUserList(
                            uiState.groupedUsers,
                            onUserClick,
                            onFavouriteUserClick
                        )
                    } else {
                        UserList(
                            uiState.users,
                            onUserClick,
                            onFavouriteUserClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserList(
    users: List<GitHubUser>,
    onUserClick: (GitHubUser) -> Unit,
    onFavouriteUserClick: (GitHubUser) -> Unit
) {
    val listState = rememberForeverLazyListState(key = "user_list")
    LazyColumn(state = listState) {
        items(
            items = users,
            key = { it.id }
        ) { user ->
            UserListItem(
                user,
                onUserClick,
                onFavouriteUserClick
            )
        }
    }
}

@Composable
private fun GroupedUserList(
    groupedUsers: Map<Char, List<GitHubUser>>,
    onUserClick: (GitHubUser) -> Unit,
    onFavouriteUserClick: (GitHubUser) -> Unit
) {
    val listState = rememberForeverLazyListState(key = "grouped_user_list")
    LazyColumn(state = listState) {
        groupedUsers.forEach { (header, users) ->
            stickyHeader {
               GroupHeader(header)
            }
            items(
                items = users,
                key = { it.id }
            ) { user ->
                UserListItem(
                    user,
                    onUserClick,
                    onFavouriteUserClick
                )
            }
        }
    }
}

@Composable
private fun GroupHeader(text: Char) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text.toString().uppercase(),
            maxLines = 1,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun UserListItem(
    user: GitHubUser,
    onUserClick: (GitHubUser) -> Unit,
    onFavouriteUserClick: (GitHubUser) -> Unit
) {
    val color = if (user.isFavourite) {
        Color(red = 253, green = 191, blue = 3, alpha = 255)
    } else {
        Color.Black
    }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(user.avatarUrl)
            .size(Size.ORIGINAL)
            .memoryCacheKey(user.id.toString())
            .build()
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onUserClick(user) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .placeholder(
                    visible = painter.state is AsyncImagePainter.State.Loading,
                    color = Color.Gray,
                    highlight = PlaceholderHighlight.shimmer()
                ),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.login,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                modifier = Modifier.clickable { onFavouriteUserClick(user) },
                painter = painterResource(id = R.drawable.ic_favorite),
                contentDescription = null,
                tint = color
            )
        }
    }
}