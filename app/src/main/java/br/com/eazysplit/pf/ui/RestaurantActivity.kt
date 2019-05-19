package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.Restaurant
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_restaurant.*

class RestaurantActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDB: FirebaseFirestore

    private var restaurantID: String? = null

    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseFirestore.getInstance()

        mDB.firestoreSettings = FirebaseFirestoreSettings.Builder()
                                    .setPersistenceEnabled(true).build()

        if(mAuth.currentUser == null){
            val loginIntent = Intent(this@RestaurantActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        loadRestaurant()
    }

    private fun loadRestaurant(){
        restaurantID = intent.extras.getString("RESTAURANT_ID")

        restaurantID?.let {
            val restaurantDocument = mDB.collection("restaurants").document(it)

            restaurantDocument.get().addOnCompleteListener {
                if(it.isSuccessful){
                    val restaurant = it.result?.toObject(Restaurant::class.java)
                    tvRating.text = restaurant!!.rating.toString()
                    tvRestaurantTitle.text = restaurant.name
                    tvDescription.text = restaurant.description

                    Glide.with(this)
                        .load(restaurant.url_image)
                        .into(ivRestaurantMap)
                } else{
                    Log.e("RestaurantActivity", it.exception?.message)
                }
            }
        }
    }
}
