package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.Card
import br.com.eazysplit.pf.util.Mask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_card.*
import java.util.*

class CardActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDB: FirebaseDatabase
    private lateinit var mReference: DatabaseReference

    private var cardID: String? = null

    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseDatabase.getInstance()
        mDB.setPersistenceEnabled(true)

        mReference = mDB.reference
        mReference.keepSynced(true)

        if(mAuth.currentUser == null){
            val loginIntent = Intent(this@CardActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        registerCard()
        inputMaskExpiration()
    }

    fun registerCard(){
        btSaveCard.setOnClickListener {
            if(validateFields()){
                val id = mAuth.currentUser!!.uid

                val card = mountCard()

                val cardReference =  mReference.child("users")
                    .child(id).child("card")

                if(cardID == null)
                    cardID = UUID.randomUUID().toString()

                cardReference.child(cardID!!).setValue(card)
            }

        }
    }

    private fun mountCard() : Card {
        return Card(etCvc.text.toString(), "", 0, etCardName.text.toString(), etCardNumber.text.toString(), 0)
    }

    private fun validateFields() : Boolean {
        if(etCPF.text.toString() == ""){
            Toast.makeText(this, R.string.text_fill_fields, Toast.LENGTH_LONG).show()
            etCPF.requestFocus()
            return false
        }

        if(etCvc.text.toString() == ""){
            Toast.makeText(this, R.string.text_fill_fields, Toast.LENGTH_LONG).show()
            etCvc.requestFocus()
            return false
        }

        if(etExpiration.text.toString() == ""){
            Toast.makeText(this, R.string.text_fill_fields, Toast.LENGTH_LONG).show()
            etExpiration.requestFocus()
            return false
        }

        if(etCardName.text.toString() == ""){
            Toast.makeText(this, R.string.text_fill_fields, Toast.LENGTH_LONG).show()
            etCardName.requestFocus()
            return false
        }

        if (etCardNumber.text.toString() == ""){
            Toast.makeText(this, R.string.text_fill_fields, Toast.LENGTH_LONG).show()
            etCardNumber.requestFocus()
            return false
        }

        return true
    }

    fun inputMaskExpiration(){
        etExpiration.addTextChangedListener(Mask.insert(etExpiration, "##/####"))
    }
}
