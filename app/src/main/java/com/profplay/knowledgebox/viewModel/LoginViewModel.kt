package com.profplay.knowledgebox.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.profplay.knowledgebox.view.myAuth

class LoginViewModel(application: Application): AndroidViewModel(application) {
    fun loginClick(usernameValue: String, passwordValue:String, onLoginSuccess:()->Unit){
        if(usernameValue.isNotEmpty() && passwordValue.isNotEmpty()){
            myAuth.signInWithEmailAndPassword(usernameValue,passwordValue).addOnSuccessListener{
                //goto MainActivity with login success
                onLoginSuccess()
            }.addOnFailureListener{
                Toast.makeText(getApplication(), it.localizedMessage ,Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(getApplication(), "Lütfen email ve parolayı giriniz!",Toast.LENGTH_LONG).show()
        }

    }

    fun registerClick(usernameValue: String, passwordValue:String, onLoginSuccess:()->Unit){

        if(usernameValue.isNotEmpty() && passwordValue.isNotEmpty()){
            myAuth.createUserWithEmailAndPassword(usernameValue,passwordValue).addOnSuccessListener{
                //goto MainActivity with register success
                onLoginSuccess()
            }.addOnFailureListener{
                Toast.makeText(getApplication(), it.localizedMessage ,Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(getApplication(), "Lütfen email ve parolayı giriniz!",Toast.LENGTH_LONG).show()
        }

    }

}