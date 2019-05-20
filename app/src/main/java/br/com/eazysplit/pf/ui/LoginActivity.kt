package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.eazysplit.pf.R
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

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

    private fun goToMenu() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
