package com.github.presentation.details

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.github.domain.model.GitHubUser
import com.github.domain.model.GitHubUserDetails
import com.github.presentation.ViewEvent
import com.github.presentation.ViewSideEffect
import com.github.presentation.ViewState
import com.github.presentation.list.GitHubUserListContract

class GitHubUserDetailsContract {

    @Stable
    @Immutable
    data class State(
        val isLoading: Boolean = false,
        val error: Throwable? = null,
        val userDetails: GitHubUserDetails? = null
    ) : ViewState

    @Stable
    @Immutable
    sealed class Event : ViewEvent {
        data class GetGitHubUserDetailsAction(val user: GitHubUser) : Event()
        data object ToggleFavouriteGitHubUserAction : Event()
    }

    @Stable
    @Immutable
    sealed class Effect : ViewSideEffect
}