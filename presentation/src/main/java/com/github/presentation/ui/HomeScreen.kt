package com.github.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.github.presentation.features.users.details.GitHubUserDetailsScreen
import com.github.presentation.features.users.details.GitHubUserDetailsViewModel
import com.github.presentation.features.users.list.GitHubUserListScreen
import com.github.presentation.features.users.list.GitHubUserListViewModel
import com.github.presentation.model.GitHubUserUi
import com.github.presentation.ui.theme.GitHubAppTheme

@Composable
fun HomeScreen() {
    GitHubAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "github_users_graph"
            ) {
                navigation(
                    startDestination = "github_user_list_screen",
                    route = "github_users_graph"
                ) {
                    var gitHubUser: GitHubUserUi? = null
                    composable("github_user_list_screen") { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry("github_users_graph")
                        }
                        val viewModel = hiltViewModel<GitHubUserListViewModel>(parentEntry)
                        GitHubUserListScreen(
                            viewModel,
                            onUserClick = {
                                gitHubUser = it
                                navController.navigate("github_user_details_screen")
                            }
                        )
                    }
                    composable("github_user_details_screen") { backStackEntry ->
                        gitHubUser?.let {user ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry("github_users_graph")
                            }
                            val viewModel = hiltViewModel<GitHubUserDetailsViewModel>(parentEntry)
                            GitHubUserDetailsScreen(
                                viewModel,
                                user,
                                onBackPressed = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}