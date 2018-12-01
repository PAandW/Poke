package com.paandw.poke.view.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.paandw.poke.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val SIGN_IN_REQUEST = 1

    private lateinit var presenter: LoginPresenter
    private lateinit var options: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = LoginPresenter()
        options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, options)
        firebaseAuth = FirebaseAuth.getInstance()
        updateUI(firebaseAuth.currentUser)
        btn_sign_in.setSize(SignInButton.SIZE_WIDE)

        btn_sign_in.setOnClickListener {
            initiateGoogleSignIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                initiateFirebaseAuth(account!!)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(this, "Signed in as ${user?.displayName}!", Toast.LENGTH_SHORT).show()
    }

    private fun initiateGoogleSignIn() {
        startActivityForResult(googleSignInClient.signInIntent, SIGN_IN_REQUEST)
    }

    private fun initiateFirebaseAuth(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                //Sign in was successful
                updateUI(firebaseAuth.currentUser)
            } else {
                Toast.makeText(this, "Auth failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
