package com.github.data.remote

import com.github.data.remote.model.GitHubUserDetailsDto
import com.github.data.remote.model.GitHubUserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher
) : NetworkSource {

    override suspend fun getGitHubUserList(): List<GitHubUserDto> =
        withContext(dispatcher) {
            return@withContext httpClient.get("/users").body()
        }

    override suspend fun getGitHubUserDetails(login: String): GitHubUserDetailsDto =
        withContext(dispatcher) {
            return@withContext httpClient.get("/users") {
                url {
                    appendPathSegments(login)
                }
            }.body()
        }
}