package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.Card
import br.com.eazysplit.pf.util.Mask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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

        if(intent.extras != null){
            loadForm()
        }
    }

    fun loadForm(){
        cardID = intent.extras.getString("CARD_ID")
        val id = mAuth.currentUser!!.uid

        val cardUnitReference =  mReference.child("users")
            .child(id).child("card").child(cardID!!)

        cardUnitReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    val card = dataSnapshot.getValue(Card::class.java)
                    card?.let {
                        etCardNumber.setText(it.number)
                        etCardName.setText(it.name)
                        etCvc.setText(it.codeValidate)
                        etCPF.setText(it.document)
                        val expiration = it.monthValidate
                                                .toString().padStart(2, '0') + "/" + it.yearValidate
                        etExpiration.setText(expiration)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
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

                cardReference.child(cardID!!).setValue(card).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, getString(R.string.successfull_add_card), Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this@CardActivity, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun mountCard() : Card {
        val expiration = etExpiration.text.toString().split("/")

        return Card("", etCvc.text.toString(), etCPF.text.toString(),"MasterCard", expiration[0].toInt(), etCardName.text.toString(), etCardNumber.text.toString(), expiration[1].toInt())
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
