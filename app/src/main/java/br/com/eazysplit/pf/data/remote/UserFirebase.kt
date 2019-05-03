package br.com.eazysplit.pf.data.remote

import br.com.eazysplit.pf.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserFirebase{

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mReference: DatabaseReference

    init {
        mAuth = FirebaseAuth.getInstance()
        mReference = FirebaseDatabase.getInstance().reference
    }

    fun add(user: User) {
        mAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    var firebaseUser = mAuth.currentUser
                    user.id = firebaseUser!!.uid

                }
        }

    }

    private fun addDB(user: User){
        mReference.child("users")
    }


    fun signIn(user: User){

    }

    fun isLoggedIn(): Boolean{
        return if (mAuth.currentUser != null) true else false
    }

    fun signOut(){
        mAuth.signOut()
    }
}