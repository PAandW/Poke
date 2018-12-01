package com.paandw.poke.view.login

import com.google.android.gms.auth.api.signin.GoogleSignInOptions

interface ILoginView {
    fun bindGoogleClient(options: GoogleSignInOptions)
    fun toHomeScreen()
    fun initiateGoogleSignIn()
    fun showAuthError(errorMsg: String)
}