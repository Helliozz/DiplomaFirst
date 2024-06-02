package com.example.cameraapp.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.MutableLiveData

object Utils {

    fun getBlankStringsChecker(textInput: EditText, setButtonVisibility: ()->Unit): TextWatcher {
        return object: TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (textInput.text.toString().replace(" ", "") == textInput.text.toString()) {
                    setButtonVisibility()
                } else {
                    textInput.setText(textInput.text.toString().replace(" ", ""))
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }
    }

    fun <T> updateLiveData(liveData: MutableLiveData<T>, message: T) {
        liveData.value = message
        liveData.postValue(message)
    }
}