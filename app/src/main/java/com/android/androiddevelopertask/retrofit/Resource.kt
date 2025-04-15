package com.android.androiddevelopertask.retrofit

import android.util.Log

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {

        fun <T> success(data: T?): Resource<T> {
            Log.d("Success", data.toString())
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            Log.d("Error", data.toString())
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            Log.d("Loading", data.toString())
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}