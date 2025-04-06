package com.profplay.knowledgebox.viewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.profplay.knowledgebox.view.LoginActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileDetailViewModel: ViewModel()  {
    fun navigateToLoginActivity(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    fun deleteUser(
        onSuccess: () -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        auth.currentUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.signOut()
                    onSuccess()
                } else {
                    onFailure(task.exception)
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
    }
}