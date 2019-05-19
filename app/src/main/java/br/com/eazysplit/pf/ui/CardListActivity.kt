package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.adapters.CardListAdapter
import br.com.eazysplit.pf.models.Card
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_card_list.*

class CardListActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDB: FirebaseFirestore

    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseFirestore.getInstance()

        mDB.firestoreSettings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()


        if(mAuth.currentUser == null){
            val loginIntent = Intent(this@CardListActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        loadData()
        addCard()
    }

    private fun addCard() {
        btnAdd.setOnClickListener {
            val cardIntent = Intent(this@CardListActivity, CardActivity::class.java)
            startActivity(cardIntent)
            finish()
        }
    }

    fun loadData(){
        val currentUser = mAuth.currentUser!!
        val cardsCollection = mDB.collection("users").document(currentUser.uid)
            .collection("cards")

        cardsCollection.addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                Log.e("MainActivity", "Listen failed!", e)
                return@addSnapshotListener
            }

            val cardList = ArrayList<Card>()

            if (querySnapshot != null) {
                for (doc in querySnapshot) {
                    val card = doc.toObject(Card::class.java)
                    cardList.add(card)
                }
            }

            listShow(cardList)
        }

    }

    fun listShow(cardList: MutableList<Card>){
        rvCardList.adapter = CardListAdapter(this, cardList, {
            goToEdit(it)
        }, {
            deleteCard(it)
        })

        val layoutManager = LinearLayoutManager(this)

        rvCardList.layoutManager = layoutManager
    }

    fun deleteCard(card: Card){
    }

    fun goToEdit(card: Card){
        val cardIntent = Intent(this@CardListActivity, CardActivity::class.java)
        cardIntent.putExtra("CARD_ID", card.id)
        startActivity(cardIntent)
        finish()
    }

}
