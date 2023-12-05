package com.github.presentation

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
import com.github.domain.model.GitHubUser
import com.github.presentation.details.GitHubUserDetailsScreen
import com.github.presentation.details.GitHubUserDetailsViewModel
import com.github.presentation.list.GitHubUserListScreen
import com.github.presentation.list.GitHubUserListViewModel
import com.github.presentation.theme.GitHubAppTheme

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
                    var gitHubUser: GitHubUser? = null
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