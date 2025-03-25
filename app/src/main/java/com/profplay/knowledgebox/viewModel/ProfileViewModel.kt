package com.profplay.knowledgebox.viewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.profplay.knowledgebox.view.LoginActivity

class ProfileViewModel: ViewModel() {
    fun navigateToLoginActivity(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
}