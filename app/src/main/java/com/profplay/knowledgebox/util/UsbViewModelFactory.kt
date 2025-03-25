package com.profplay.knowledgebox.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.profplay.knowledgebox.viewModel.UsbViewModel

class UsbViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsbViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsbViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
