package com.paandw.poke.view.login

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.paandw.poke.R
import com.paandw.poke.data.models.User

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
        } else {
            view.nukeSignIn()
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
                val database = FirebaseDatabase.getInstance().reference
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                val user = User()
                user.id = firebaseUser?.uid
                user.username = firebaseUser?.displayName

                database.child("users").child(user.id).runTransaction(object : Transaction.Handler {
                    override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                        view.toHomeScreen()
                    }

                    override fun doTransaction(data: MutableData): Transaction.Result {
                        val newUser = data.getValue(User::class.java)
                        return if (newUser != null) {
                            Transaction.success(data)
                        } else {
                            data.value = user
                            Transaction.success(data)
                        }
                    }
                })
            } else {
                view.showAuthError(it.exception?.message!!)
            }
        }
    }

}