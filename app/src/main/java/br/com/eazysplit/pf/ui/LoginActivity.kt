package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.eazysplit.pf.R
import com.facebook.*
import com.facebook.login.*
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN_REQUEST_CODE = 2

    private val TAG = "LoginActivity"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null) {
            goToMenu()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_login)

        setupButtonLogin()
        setupSignUpOtherCustomer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GOOGLE_SIGN_IN_REQUEST_CODE -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    handleGoogleAccessToken(account)
                } catch (e: ApiException) {
                    Log.w(TAG, "signInResult:failed code=" + e.message)
                }
            }
            else -> {
                callbackManager.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun setupButtonLogin() {
        btLogin.setOnClickListener {
            if (validateFields()) {
                mAuth.signInWithEmailAndPassword(etUserName.text.toString(), etPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            goToMenu()
                        } else {
                            Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    private fun setupSignUpOtherCustomer() {
        tvOtherCustomer.setOnClickListener {
            startActivity(Intent(this, CustomerActivity::class.java))
        }
    }

    private fun validateFields() : Boolean {
        if (etUserName.text.toString() == "" || etPassword.text.toString() == "") {
            Toast.makeText(this, R.string.text_fill_fields, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun handleGoogleAccessToken(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.getIdToken(), null)
        handleAccessCredential(credential)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        handleAccessCredential(credential)
    }

    private fun handleAccessCredential(credential: AuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, CustomerActivity::class.java))
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun goToMenu() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
