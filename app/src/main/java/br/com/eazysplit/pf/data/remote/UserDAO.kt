package br.com.eazysplit.pf.data.remote

import br.com.eazysplit.pf.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserDAO{

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDB: FirebaseDatabase

    init {
        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseDatabase.getInstance()
    }

    fun add(user: User){


    }

    fun isLoggedIn(): Boolean{
        return if (mAuth.currentUser != null) true else false
    }
}