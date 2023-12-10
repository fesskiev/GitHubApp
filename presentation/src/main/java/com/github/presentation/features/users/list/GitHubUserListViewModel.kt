package com.github.presentation.features.users.list

import androidx.lifecycle.viewModelScope
import com.github.domain.usecase.GetGitHubUsersUseCase
import com.github.domain.usecase.GetGroupedGitHubUsersUseCase
import com.github.domain.usecase.UpdateGitHubUserFavouriteUseCase
import com.github.presentation.core.BaseViewModel
import com.github.presentation.features.users.list.GitHubUserListContract.Event
import com.github.presentation.features.users.list.GitHubUserListContract.State
import com.github.presentation.features.users.list.GitHubUserListContract.Effect
import com.github.presentation.mapper.mapToUiModel
import com.github.presentation.model.GitHubUserUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubUserListViewModel @Inject constructor(
    private val getGitHubUsersUseCase: GetGitHubUsersUseCase,
    private val getGroupedGitHubUsersUseCase: GetGroupedGitHubUsersUseCase,
    private val updateGitHubUserFavouriteUseCase: UpdateGitHubUserFavouriteUseCase,
) : BaseViewModel<Event, State, Effect>() {

    override val initialState: State = State()

    override fun handleViewEvents(viewEvent: Event) {
        when (viewEvent) {
            is Event.GetGitHubUsersAction -> getGitHubUsersByFilter()
            is Event.ToggleFilterAction -> toggleFilter()
            is Event.GitHubUserClickAction -> openGitHubUserDetailsScreen(viewEvent.user)
            is Event.ToggleFavouriteGitHubUserAction -> toggleGitHubUserFavourite(viewEvent.user)
        }
    }

    private fun getGitHubUsersByFilter() {
        val isFilterActive = viewState.value.isFilterActive
        if (isFilterActive) {
            getGroupedGitHubUsers()
        } else {
            getGitHubUsers()
        }
    }

    private fun toggleGitHubUserFavourite(user: GitHubUserUi) {
        val login = user.login
        val isFavourite = !user.isFavourite
        viewModelScope.launch {
            updateGitHubUserFavouriteUseCase.invoke(login, isFavourite)
                .onSuccess { updateGitHubUserFavourite(login, isFavourite) }
                .onFailure { showError(it) }
        }
    }

    private fun getGroupedGitHubUsers() {
        viewModelScope.launch {
            showProgress()
            getGroupedGitHubUsersUseCase()
                .onSuccess { users -> showGroupedGitHubUsers(users.mapToUiModel()) }
                .onFailure { showError(it) }
        }
    }

    private fun getGitHubUsers() {
        viewModelScope.launch {
            showProgress()
            getGitHubUsersUseCase()
                .onSuccess { users -> showGitHubUsers(users.map { it.mapToUiModel() }) }
                .onFailure { showError(it) }
        }
    }

    private fun updateGitHubUserFavourite(login: String, isFavourite: Boolean) {
        val isFilterActive = viewState.value.isFilterActive
        if (isFilterActive) {
            setViewState {
                copy(
                    groupedUsers = viewState.value.groupedUsers.values
                        .flatten()
                        .map { user ->
                            if (user.login == login) {
                                user.copy(isFavourite = isFavourite)
                            } else {
                                user
                            }
                        }
                        .groupBy { user ->
                            user.login[0]
                        }
                )
            }
        } else {
            setViewState {
                copy(
                    users = viewState.value.users.map { user ->
                        if (user.login == login) {
                            user.copy(isFavourite = isFavourite)
                        } else {
                            user
                        }
                    }
                )
            }
        }
    }

    private fun toggleFilter() {
        val isFilterActive = !viewState.value.isFilterActive
        if (isFilterActive) {
            getGroupedGitHubUsers()
        } else {
            getGitHubUsers()
        }
    }

    private fun showGroupedGitHubUsers(groupUsers: Map<Char, List<GitHubUserUi>>) {
        setViewState {
            copy(
                isLoading = false,
                error = null,
                isFilterActive = true,
                users = listOf(),
                groupedUsers = groupUsers
            )
        }
    }

    private fun showGitHubUsers(users: List<GitHubUserUi>) {
        setViewState {
            copy(
                isLoading = false,
                error = null,
                isFilterActive = false,
                users = users,
                groupedUsers = mapOf()
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

    private fun openGitHubUserDetailsScreen(user: GitHubUserUi) {
        setViewEffect {
            Effect.ShowGitHubUserDetailsScreen(user)
        }
    }
}