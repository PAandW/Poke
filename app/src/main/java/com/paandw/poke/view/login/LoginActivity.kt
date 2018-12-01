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
import com.paandw.poke.view.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), ILoginView {

    private val SIGN_IN_REQUEST = 1

    private lateinit var presenter: LoginPresenter
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = LoginPresenter()

        btn_sign_in.setSize(SignInButton.SIZE_WIDE)

        btn_sign_in.setOnClickListener {
            presenter.startSignInProcess()
        }

        presenter.start(this, getString(R.string.firebase_web_client_id))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                presenter.startAuthentication(account!!)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    override fun bindGoogleClient(options: GoogleSignInOptions) {
        googleSignInClient = GoogleSignIn.getClient(this, options)
    }

    override fun toHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun initiateGoogleSignIn() {
        startActivityForResult(googleSignInClient.signInIntent, SIGN_IN_REQUEST)
    }

    override fun showAuthError(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
    }
}
