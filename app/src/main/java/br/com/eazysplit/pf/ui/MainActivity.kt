package br.com.eazysplit.pf.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.adapters.RestaurantListAdapter
import br.com.eazysplit.pf.models.Restaurant
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_card.*
import kotlinx.android.synthetic.main.activity_card.navMenu
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private lateinit var db: FirebaseDatabase

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onStart() {
        super.onStart()

        db = FirebaseDatabase.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navMenu.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        loadData()
    }

    private fun loadData(){
        val restaurantReference = db.reference.child("restaurants")

        restaurantReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    val restaurantList = ArrayList<Restaurant>()

                    for (singleSnapshot in dataSnapshot.children){
                        val restaurant = singleSnapshot.getValue(Restaurant::class.java)
                        restaurant?.let { restaurantList.add(it) }
                    }

                    listShow(restaurantList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun listShow(restaurant_row: List<Restaurant>){
        rvListRestaurante.adapter = RestaurantListAdapter(this, restaurant_row) {
            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
        }
    }

}

