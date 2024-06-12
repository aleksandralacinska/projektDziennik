package com.example.dziennikmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dziennikmvvm.model.DziennikData

class DziennikViewModel : ViewModel() {

    private val _dziennikData = MutableLiveData<DziennikData>()
    val dziennikData: LiveData<DziennikData> get() = _dziennikData

    init {
        _dziennikData.value = DziennikData()
    }
}
