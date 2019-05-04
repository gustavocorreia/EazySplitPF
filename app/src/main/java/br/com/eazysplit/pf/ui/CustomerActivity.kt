package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_customer.*
import java.util.*

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
            etEmail.setText(currentUser?.email)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        registerCustomer()
        loadImage()
    }

    fun registerCustomer(){
        btRegister.setOnClickListener {
            if(validateFields()){
                val user = mountUser()

                val currentUser = mAuth.currentUser
                if(currentUser == null){
                    mAuth.createUserWithEmailAndPassword(user.email, user.password)
                        .addOnCompleteListener {
                            if(it.isSuccessful){

                                completeRegister(user)
                            } else {
                                Toast.makeText(this@CustomerActivity, it.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            }
        }
    }

    private fun completeRegister(user: User){
        user.id = mAuth.currentUser!!.uid

        mDB.getReference("Users")
            .child(user.id)
            .setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    Toast.makeText(this, R.string.text_user_success, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, R.string.text_user_error, Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun loadImage(){
        ivCustomer.setOnClickListener {

        }
    }

    private fun mountUser() : User{
        val user = User ("", etName.text.toString(), etEmail.text.toString(), etPhone.text.toString(), Date(etBirthDate.text.toString()), etPassword.text.toString(), null, null, Date())

        return user
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
