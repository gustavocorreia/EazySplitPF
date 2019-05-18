package br.com.eazysplit.pf.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import br.com.eazysplit.pf.HomeFragment
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.SettingsFragment
import br.com.eazysplit.pf.adapters.RestaurantListAdapter
import br.com.eazysplit.pf.models.Restaurant
import com.google.firebase.database.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_card.*
import kotlinx.android.synthetic.main.activity_card.navMenu
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.activity_main.rvListRestaurante as rvListRestaurante1



class MainActivity : AppCompatActivity() {


    // private lateinit var textMessage: TextView
    private lateinit var db: FirebaseFirestore


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                addFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                addFragment(SettingsFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()

        navMenu.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // loadData()
    }

    private fun addFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()

    }

    private fun loadData(){
        val restaurantCollection = db.collection("restaurants")

        restaurantCollection.addSnapshotListener { documentSnapshots, e ->
            if (e != null) {
                Log.e("MainActivity", "Listen failed!", e)
                return@addSnapshotListener
            }

            val restaurantList = ArrayList<Restaurant>()

            if (documentSnapshots != null) {
                for (doc in documentSnapshots) {
                    val note = doc.toObject(Restaurant::class.java)
                    restaurantList.add(note)
                }
            }

            listShow(restaurantList)

        }

    }

    private fun listShow(restaurant_row: List<Restaurant>){
        rvListRestaurante.adapter = RestaurantListAdapter(this, restaurant_row) {
            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
        }
    }

}