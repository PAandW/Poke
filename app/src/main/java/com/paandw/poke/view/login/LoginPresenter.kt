package com.paandw.poke.view.login

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.paandw.poke.R

class LoginPresenter {

    private lateinit var options: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var view: ILoginView

    fun start(view: ILoginView, firebaseWebId: String){
        this.view = view
        options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(firebaseWebId)
                .requestEmail()
                .build()
        firebaseAuth = FirebaseAuth.getInstance()

        view.bindGoogleClient(options)

        if (firebaseAuth.currentUser != null) {
            view.toHomeScreen()
        }
    }

    fun startSignInProcess() {
        view.initiateGoogleSignIn()
    }

    fun startAuthentication(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                //Sign in was successful
                view.toHomeScreen()
            } else {
                view.showAuthError(it.exception?.message!!)
            }
        }
    }

}