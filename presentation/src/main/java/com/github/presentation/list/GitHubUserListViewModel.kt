package com.github.presentation.list

import androidx.lifecycle.viewModelScope
import com.github.domain.model.GitHubUser
import com.github.domain.usecase.GetGitHubUserListUseCase
import com.github.domain.usecase.UpdateGitHubUserFavouriteUseCase
import com.github.presentation.BaseViewModel
import com.github.presentation.list.GitHubUserListContract.Event
import com.github.presentation.list.GitHubUserListContract.State
import com.github.presentation.list.GitHubUserListContract.Effect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubUserListViewModel @Inject constructor(
    private val getGitHubUserListUseCase: GetGitHubUserListUseCase,
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

    private fun toggleGitHubUserFavourite(user: GitHubUser) {
        val login = user.login
        val isFavourite = !user.isFavourite
        viewModelScope.launch {
            updateGitHubUserFavouriteUseCase.invoke(login, isFavourite)
                .onSuccess { updateGitHubUserFavourite(login, isFavourite) }
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
                        }.groupBy { user ->
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

    private fun getGroupedGitHubUsers() {
        viewModelScope.launch {
            showProgress()
            getGitHubUserListUseCase()
                .onSuccess {
                    showGroupedGitHubUsers(
                        it.groupBy { user ->
                            user.login[0]
                        }
                    )
                }
                .onFailure { showError(it) }
        }
    }

    private fun getGitHubUsers() {
        viewModelScope.launch {
            showProgress()
            getGitHubUserListUseCase()
                .onSuccess { showGitHubUsers(it) }
                .onFailure { showError(it) }
        }
    }

    private fun showGroupedGitHubUsers(groupUsers: Map<Char, List<GitHubUser>>) {
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

    private fun showGitHubUsers(users: List<GitHubUser>) {
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

    private fun openGitHubUserDetailsScreen(user: GitHubUser) {
        setViewEffect {
            Effect.ShowGitHubUserDetailsScreen(user)
        }
    }
}