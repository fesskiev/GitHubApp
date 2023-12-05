package com.github.presentation.list

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.github.domain.model.GitHubUser
import com.github.presentation.ViewEvent
import com.github.presentation.ViewSideEffect
import com.github.presentation.ViewState

class GitHubUserListContract {

    @Stable
    @Immutable
    data class State(
        val isLoading: Boolean = false,
        val isFilterActive: Boolean = false,
        val error: Throwable? = null,
        val users: List<GitHubUser> = listOf(),
        val groupedUsers: Map<Char, List<GitHubUser>> = mapOf()
    ) : ViewState

    @Stable
    @Immutable
    sealed class Event : ViewEvent {
        data object GetGitHubUsersAction : Event()
        data object ToggleFilterAction : Event()
        data class GitHubUserClickAction(val user: GitHubUser) : Event()
        data class ToggleFavouriteGitHubUserAction(val user: GitHubUser) : Event()
    }

    @Stable
    @Immutable
    sealed class Effect : ViewSideEffect {
        data class ShowGitHubUserDetailsScreen(val user: GitHubUser) : Effect()
    }
}