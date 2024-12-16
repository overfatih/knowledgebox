package com.profplay.knowledgebox.roomdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.profplay.knowledgebox.viewModel.MainViewModel

class MainViewModelFactory(private val repository: CityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}