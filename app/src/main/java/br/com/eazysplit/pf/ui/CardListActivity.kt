package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.adapters.CardListAdapter
import br.com.eazysplit.pf.models.Card
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_card_list.*

class CardListActivity : AppCompatActivity() {

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
    }

    fun loadData(){
        val currentUser = mAuth.currentUser!!
        val cardReference = mReference.child("users")
                                                    .child(currentUser.uid).child("cards")

        cardReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    val cardList = ArrayList<Card>()

                    for(singleSnapshot in dataSnapshot.children){
                        val card = singleSnapshot.getValue(Card::class.java)

                        card?.let {
                            it.id = singleSnapshot.key!!
                            cardList.add(it)
                        }
                    }

                    listShow(cardList)
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    fun listShow(cardList: List<Card>){
        rvCardList.adapter = CardListAdapter(this, cardList, {
            goToEdit(it)
        })
    }

    fun goToEdit(card: Card){
        
    }
}
