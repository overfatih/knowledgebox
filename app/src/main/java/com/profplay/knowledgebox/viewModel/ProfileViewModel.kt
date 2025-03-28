package com.profplay.knowledgebox.viewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.profplay.knowledgebox.view.LoginActivity

class ProfileViewModel: ViewModel() {
    fun navigateToLoginActivity(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

    fun getCurrentUserName(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.displayName
    }
}