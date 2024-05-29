package com.example.cameraapp.data

sealed class AuthenticationStatus {

    object Success : AuthenticationStatus()

    data class Error(val message: String) : AuthenticationStatus()

    object Progress: AuthenticationStatus()

}

sealed class DownloadStatus<out T> {

    data class Success<T>(val result: T) : DownloadStatus<T>()

    data class Error(val message: String) : DownloadStatus<Nothing>()

    data class WeakProgress(val message: String): DownloadStatus<Nothing>()

    object Progress: DownloadStatus<Nothing>()

}
