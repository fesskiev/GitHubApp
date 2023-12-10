package com.github.presentation.features.users.details

import androidx.lifecycle.viewModelScope
import com.github.domain.model.GitHubUserDetails
import com.github.domain.usecase.GetGitHubUserDetailsUseCase
import com.github.domain.usecase.UpdateGitHubUserFavouriteUseCase
import com.github.presentation.core.BaseViewModel
import com.github.presentation.features.users.details.GitHubUserDetailsContract.Event
import com.github.presentation.features.users.details.GitHubUserDetailsContract.Effect
import com.github.presentation.features.users.details.GitHubUserDetailsContract.State
import com.github.presentation.model.GitHubUserUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubUserDetailsViewModel @Inject constructor(
    private val getGitHubUserDetailsUseCase: GetGitHubUserDetailsUseCase,
    private val updateGitHubUserFavouriteUseCase: UpdateGitHubUserFavouriteUseCase
) : BaseViewModel<Event, State, Effect>() {

    override val initialState: State = State()

    override fun handleViewEvents(viewEvent: Event) {
        when (viewEvent) {
            is Event.GetGitHubUserDetailsAction -> getGitHubUserDetails(viewEvent.user)
            is Event.ToggleFavouriteGitHubUserAction -> toggleGitHubUserFavourite()
        }
    }

    private fun toggleGitHubUserFavourite() {
        val userDetails = viewState.value.userDetails
        if (userDetails != null) {
            val login = userDetails.login
            val isFavourite = !userDetails.isFavourite
            viewModelScope.launch {
                updateGitHubUserFavouriteUseCase.invoke(login, isFavourite)
                    .onSuccess { updateGitHubUserDetailsFavourite(isFavourite) }
                    .onFailure { showError(it) }
            }
        }
    }

    private fun getGitHubUserDetails(user: GitHubUserUi) {
        viewModelScope.launch {
            showProgress()
            getGitHubUserDetailsUseCase(user.login)
                .onSuccess { showGitHubUserDetails(user, it) }
                .onFailure { showError(it) }
        }
    }

    private fun updateGitHubUserDetailsFavourite(isFavourite: Boolean) {
        setViewState {
            copy(
                isLoading = false,
                error = null,
                userDetails = userDetails?.copy(isFavourite = isFavourite)
            )
        }
    }

    private fun showGitHubUserDetails(user: GitHubUserUi, userDetails: GitHubUserDetails) {
        setViewState {
            copy(
                isLoading = false,
                error = null,
                userDetails = userDetails.copy(isFavourite = user.isFavourite)
            )
        }
    }

    private fun showError(throwable: Throwable) {
        setViewState {
            copy(
                isLoading = false,
                error = throwable
            )
        }
    }

    private fun showProgress() {
        setViewState {
            copy(
                isLoading = true,
                error = null
            )
        }
    }
}