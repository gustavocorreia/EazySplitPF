package br.com.eazysplit.pf.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.eazysplit.pf.models.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_customer.*
import java.util.*
import br.com.eazysplit.pf.R
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.firebase.Timestamp
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.io.IOException


class CustomerActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDB: FirebaseFirestore
    private lateinit var mStorage: FirebaseStorage

    private var update = false

    companion object {
        val CAMERA_REQUEST_CODE = 71
        val COLLECTION_USERS = "users"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        registerCustomer()
        setListenerImageCustomer()
    }


    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseFirestore.getInstance()
        mDB.firestoreSettings = FirebaseFirestoreSettings.Builder()
                                                         .setPersistenceEnabled(true).build()
        mStorage = FirebaseStorage.getInstance()

        if (mAuth.currentUser != null) {
            update = true
            getUser()
        }
    }

    private fun registerCustomer(){
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
                } else{
                    completeRegister(user)
                }

            }
        }
    }

    fun setListenerImageCustomer() {
        ivCustomer.setOnClickListener {

            if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE)
            } else {
                val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intentCamera, CAMERA_REQUEST_CODE)
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("CAMERA_REQUEST", "Permission has been denied by user")
                } else {
                    val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intentCamera, CAMERA_REQUEST_CODE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (data != null) {
                    val bundle = data.extras
                    if (bundle != null) {
                        val img = bundle.get("data") as Bitmap
                        ivCustomer.setImageBitmap(img)
                    }
                }
            }
        }
    }

    private fun completeRegister(user: User){
        val uid = mAuth.currentUser?.uid ?: ""
        user.id = uid

        if (ivCustomer != null) {
            val imageByteArray = imageToBitmap(ivCustomer)
            val storageRef = mStorage.reference.child("profile_images").child("${uid}.png")
            storageRef.delete().addOnCompleteListener {
                storageRef.putBytes(imageByteArray)
                    .addOnCompleteListener {
                        storageRef.downloadUrl.addOnCompleteListener {
                            Picasso.get().load(it.result).into(ivCustomer)
                            performUserChange(user)
                        }
                    }
            }
        } else {
            performUserChange(user)
        }

    }

    private fun performUserChange(user: User) {
        val currentUser = mAuth.currentUser

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(user.name)
            .setPhotoUri(Uri.parse(user.url_image))
            .build()

        currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    performUserChangeOthersData(user)
                }
            }
    }

    private fun performUserChangeOthersData(user: User) {
        val docData = HashMap<String, Any>()
        docData.put("birthDate", user.birthDate)
        docData.put("phoneNumber", user.phoneNumber)

        mDB.collection(COLLECTION_USERS).document(user.id).set(docData)
            .addOnCompleteListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    private fun mountUser() : User{
        val user = User ("", etName.text.toString(), etEmail.text.toString(), etPhone.text.toString(), Date(etBirthDate.text.toString()), etPassword.text.toString(), "", null, Date())

        return user
    }

    private fun validateFields() : Boolean{
        if (etName.text.toString() == ""
            || etEmail.text.toString() == ""
            || etPhone.text.toString() == ""
            || etBirthDate.text.toString() == ""
            || (etPassword.text.toString() == "" && !update)
            || (etPasswordConfirm.text.toString() == "" && !update)) {
            Toast.makeText(this, R.string.text_fill_fields, Toast.LENGTH_LONG).show()
            return false
        }

        if(etPassword.text.toString() != etPasswordConfirm.text.toString() && !update){
            Toast.makeText(this, R.string.text_divergent_passwords, Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun imageToBitmap(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        return stream.toByteArray()
    }

    private fun getUser() {
        val uid = mAuth.currentUser?.uid ?: ""

        etName.setText(mAuth.currentUser?.displayName)
        etEmail.setText(mAuth.currentUser?.email)
        etPassword.visibility = View.INVISIBLE
        etPasswordConfirm.visibility = View.INVISIBLE

        Picasso.get().load(mAuth.currentUser?.photoUrl).into(ivCustomer)

        mDB.collection(COLLECTION_USERS).document(uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.getResult()
                val data = document?.getData()
                val phone = data?.get("phoneNumber") as? String
                val birthDate = data?.get("birthDate") as? Timestamp

                val sdf = SimpleDateFormat("dd/MM/yyyy")

                etPhone.setText(phone)
                etBirthDate.setText(sdf.format(birthDate?.toDate()))
            }
        }

        btRegister.setText("Alterar")
    }


}
