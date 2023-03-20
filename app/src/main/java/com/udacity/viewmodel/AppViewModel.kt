package com.udacity.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AppViewModel(application: Application) : AndroidViewModel(application) {


    private var _isDownloadEventComplete = MutableLiveData<Boolean>()
    val isDownloadEventComplete : LiveData<Boolean>
        get() = _isDownloadEventComplete



    init {
        Log.i("AppViewModel","AppViewModel init")
        _isDownloadEventComplete.value = false
    }

    fun updateDownloadStatus(status : Boolean){
        _isDownloadEventComplete.value = status
    }
}