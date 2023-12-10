package com.github.presentation.features.users.details

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.github.domain.model.GitHubUserDetails
import com.github.presentation.core.ViewEvent
import com.github.presentation.core.ViewSideEffect
import com.github.presentation.core.ViewState
import com.github.presentation.model.GitHubUserUi

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
        data class GetGitHubUserDetailsAction(val user: GitHubUserUi) : Event()
        data object ToggleFavouriteGitHubUserAction : Event()
    }

    @Stable
    @Immutable
    sealed class Effect : ViewSideEffect
}