package com.acun.quranicplus.data.remote.response

sealed class Resource<T>(val data: T? = null, val message: String = "") {
    class Loading<T>: Resource<T>()
    class Success<T>(data: T, message: String): Resource<T>(data, message)
    class Failed<T>(message: String): Resource<T>(message = message)
}