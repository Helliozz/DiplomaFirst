package com.example.cameraapp.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cameraapp.data.AuthenticationStatus
import com.example.cameraapp.models.CDataEntity
import com.example.cameraapp.models.CDataRepository
import com.example.cameraapp.models.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.Utf8
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val rImplementation: FirebaseRepository,
    private val sPreferences: SharedPreferences
) : ViewModel() {
    var authState: MutableLiveData<AuthenticationStatus> = MutableLiveData()

    var currentPhotoIndex = 0

    private lateinit var cRepository: CDataRepository
    private lateinit var cameraData: LiveData<List<CDataEntity>>

    init {
        Log.d("TAG", "Created a view model for the outer app segment successfully.")
    }

    fun initializeRoomDatabase(application: Application) {
        if (!isUserSingedIn()) {
            throw(Exception("Attempt to initialize a DB even though the user wasn't signed in."))
        }
        cRepository = CDataRepository(application, getUserEmail()!!)
        cameraData = cRepository.getAllData()
    }

    fun getAllCData(): LiveData<List<CDataEntity>> {
        return cameraData
    }

    fun insertPhoto(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapByteArray = stream.toByteArray()
        stream.close()

        var id = -1
        getAllCData().value!!.forEach {
            if (it.id > id) {
                id = it.id
            }
        }
        id += 1

        currentPhotoIndex = getAllCData().value!!.size
        cRepository.insert(CDataEntity(id, bitmapByteArray))
    }

    fun getPhoto(index: Int): Bitmap {
        val bitmapByteArray = getAllCData().value!![index].value
        return BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.size)
    }

    fun deletePhoto(cdata: CDataEntity) {
        if (currentPhotoIndex == cameraData.value!!.lastIndex) {
            currentPhotoIndex -= 1
        }
        cRepository.delete(cdata)
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