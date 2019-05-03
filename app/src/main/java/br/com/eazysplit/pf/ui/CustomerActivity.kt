package br.com.eazysplit.pf.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_customer.*

class CustomerActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDB: FirebaseDatabase


    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseDatabase.getInstance()

        loadForm()
    }

    private fun loadForm(){
        val currentUser = mAuth.currentUser
        if(currentUser != null) {
            etEmail.setText(currentUser!!.email)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        registerCustomer()
    }

    fun registerCustomer(){
        btRegister.setOnClickListener {
            if(validateFields()){

            }
        }
    }

    private fun validateFields() : Boolean{
        if (etName.text.toString() == ""
            || etEmail.text.toString() == ""
            || etPassword.text.toString() == ""
            || etPhone.text.toString() == ""
            || etBirthDate.text.toString() == ""
            || etPasswordConfirm.text.toString() == "") {
            Toast.makeText(this, R.string.text_fill_fields, Toast.LENGTH_LONG).show()
            return false
        }

        if(etPassword.text.toString() != etPasswordConfirm.text.toString()){
            Toast.makeText(this, R.string.text_divergent_passwords, Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}
