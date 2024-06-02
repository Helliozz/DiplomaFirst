package com.example.cameraapp.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cameraapp.data.AuthenticationStatus
import com.example.cameraapp.models.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val rImplementation: FirebaseRepository,
    private val sPreferences: SharedPreferences,
    application: Application
) : AndroidViewModel(application) {
    var authState: MutableLiveData<AuthenticationStatus> = MutableLiveData()
    var cameraData: MutableLiveData<ArrayList<Bitmap>> = MutableLiveData(arrayListOf())

    var currentPhotoIndex = 0

    init {
        Log.d("TAG", "Created a view model for the outer app segment successfully.")
    }

    fun insertPhoto(bitmap: Bitmap) {
        cameraData.value!!.add(bitmap)
        cameraData.postValue(cameraData.value)
        currentPhotoIndex = cameraData.value!!.size
    }

    fun deletePhoto(bitmap: Bitmap) {
        if (currentPhotoIndex == cameraData.value!!.size) {
            currentPhotoIndex -= 1
        }
        cameraData.value!!.remove(bitmap)
        cameraData.postValue(cameraData.value)
    }

    fun resetAuthState() {
        authState = MutableLiveData()
    }

    fun isUserSingedIn(): Boolean {
        if (rImplementation.getCurrentUser() != null) {
            return true
        }
        return false
    }

    fun getUserEmail(): String? {
        if (!isUserSingedIn()) {
            return null
        }
        return rImplementation.getCurrentUser()!!.email
    }

    fun signIn(email: String, password: String, newAccount: Boolean) {
        authState.value = AuthenticationStatus.Progress
        rImplementation.signIn(email, password, newAccount).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                authState.value = AuthenticationStatus.Success
            } else {
                authState.value = AuthenticationStatus.Error(task.exception!!.message.toString())
            }
        }
    }

    fun signOut() {
        rImplementation.signOut()
    }

    fun <T> editPreferences(preference: String, value: T) {
        val sEdit: SharedPreferences.Editor
        if (preference.contains("_BOOL")) {
            sEdit = sPreferences.edit().putBoolean(preference, (value as Boolean))
        } else {
            sEdit = sPreferences.edit().putString(preference, (value as String))
        }
        sEdit.apply()
    }

    fun <T> getPreference(preference: String, defValue: T): T {
        if (preference.contains("_BOOL")) {
            return (sPreferences.getBoolean(preference, (defValue as Boolean)) as T)
        } else {
            return (sPreferences.getString(preference, (defValue as String)) as T)
        }
    }
}