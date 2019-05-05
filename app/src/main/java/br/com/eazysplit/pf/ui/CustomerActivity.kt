package br.com.eazysplit.pf.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ProgressBar
import android.widget.Toast
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.activity_customer.etPassword
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import java.util.*



class CustomerActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDB: FirebaseDatabase
    private lateinit var mStorage: FirebaseStorage
    private var image_path: Uri? = null

    companion object {
        val IMAGE_REQUEST_CODE = 71
    }


    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseDatabase.getInstance()
        mStorage = FirebaseStorage.getInstance()

        loadForm()
    }

    private fun loadForm(){
        val currentUser = mAuth.currentUser
        if(currentUser != null) {
            etName.setText(currentUser.displayName)
            etEmail.setText(currentUser.email)

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
        val profileChangeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(etName.text.toString()).build()

        mAuth.currentUser?.updateProfile(profileChangeRequest)

        mDB.getReference("Users")
            .child(user.id)
            .setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    user.url_image = uploadProfileImage(user)

                    Toast.makeText(this, R.string.text_user_success, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(this, R.string.text_user_error, Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun uploadProfileImage(user: User) : String{

        var image_firebase_path = "profile_images/" + user.id + ".png"
        val imageReference = mStorage.reference.child(image_firebase_path)
        image_path?.let {
            imageReference.putFile(it).addOnFailureListener{
                Toast.makeText(this, R.string.text_upload_image_error, Toast.LENGTH_SHORT).show()
            }
        }

        return image_firebase_path
    }

    private fun loadImage(){
        ivCustomer.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, R.string.text_select_profile_picture.toString()), IMAGE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQUEST_CODE
            && resultCode == RESULT_OK
            && data != null
            && data.data != null){
            try {
                image_path = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_path);

                ivCustomer.setImageBitmap(bitmap)

            } catch (e: Exception){
                Toast.makeText(this, R.string.text_image_error, Toast.LENGTH_SHORT).show()
            }
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
