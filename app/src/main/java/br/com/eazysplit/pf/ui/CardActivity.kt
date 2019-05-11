package br.com.eazysplit.pf.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.eazysplit.pf.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_card.*

class CardActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDB: FirebaseDatabase
    private lateinit var mReference: DatabaseReference

    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseDatabase.getInstance()
        mDB.setPersistenceEnabled(true)

        mReference = mDB.reference
        mReference.keepSynced(true)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        registerCard()
    }

    fun registerCard(){
        btSaveCard.setOnClickListener {

        }
    }
}
