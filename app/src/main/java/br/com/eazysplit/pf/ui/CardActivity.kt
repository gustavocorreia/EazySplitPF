package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.Card
import br.com.eazysplit.pf.util.Mask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_card.*
import java.util.*

class CardActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDB: FirebaseFirestore

    private var cardID: String? = null

    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseFirestore.getInstance()

        mDB.firestoreSettings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

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
        inputMaskCPF()

        if(intent.extras != null){
            loadForm()
        }
    }

    fun loadForm(){
        cardID = intent.extras.getString("CARD_ID")
        val id = mAuth.currentUser!!.uid

        val cardDocument = mDB.collection("users").document(id)
                                                .collection("cards").document(cardID!!)

        cardDocument.get().addOnCompleteListener {
            if(it.isSuccessful){
                val card = it.result?.toObject(Card::class.java)
                etCardNumber.setText(card!!.number)
                etCardName.setText(card.name)
                etCvc.setText(card.codeValidate)
                etCPF.setText(card.document)
                val expiration = card.monthValidate
                    .toString().padStart(2, '0') + "/" + card.yearValidate
                etExpiration.setText(expiration)
            } else{
                Log.e("CardActivity", it.exception?.message)
            }
        }

    }

    fun registerCard(){
        btSaveCard.setOnClickListener {
            if(validateFields()){
                val id = mAuth.currentUser!!.uid

                val card = mountCard()

                val cardCollection = mDB.collection("users").document(id)
                                .collection("cards")

                if(cardID == null)
                    cardID = UUID.randomUUID().toString()

                cardCollection.document(cardID!!).set(card).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, getString(R.string.successfull_add_card), Toast.LENGTH_SHORT).show()

                        val cardListIntent = Intent(this@CardActivity, CardListActivity::class.java)
                        startActivity(cardListIntent)
                        finish()
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

    fun inputMaskCPF(){
        etCPF.addTextChangedListener(Mask.insert(etCPF, "###.###.###-##"))
    }

    fun inputMaskCardNumber(){
        etCardNumber.addTextChangedListener(Mask.insert(etCardNumber, "#### #### #### ####"))
    }
}
