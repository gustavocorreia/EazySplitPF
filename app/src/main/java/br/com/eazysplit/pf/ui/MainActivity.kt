package br.com.eazysplit.pf.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import br.com.eazysplit.pf.HomeFragment
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.SettingsFragment
import kotlinx.android.synthetic.main.activity_card.navMenu



class MainActivity : AppCompatActivity() {


    // private lateinit var textMessage: TextView
    //private lateinit var db: FirebaseFirestore


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
        addFragment(HomeFragment())


        navMenu.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

    override fun onStart() {
        super.onStart()

        //db = FirebaseFirestore.getInstance()
        //loadData()
    }

    private fun addFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()

    }

    /*private fun loadData(){
        val restaurantCollection = db.collection("restaurants")

        restaurantCollection.addSnapshotListener { documentSnapshots, e ->
            if (e != null) {
                Log.e("MainActivity", "Listen failed!", e)
                return@addSnapshotListener
            }

            val restaurantList = ArrayList<Restaurant>()

            if (documentSnapshots != null) {
                for (doc in documentSnapshots) {
                    val restaurant = doc.toObject(Restaurant::class.java)
                    restaurantList.add(restaurant)
                }
            }

            listShow(restaurantList)

        }

    }

    private fun listShow(restaurant_row: List<Restaurant>){
        rvListRestaurante.adapter = RestaurantListAdapter(this, restaurant_row) {
            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
        }
    }*/

}