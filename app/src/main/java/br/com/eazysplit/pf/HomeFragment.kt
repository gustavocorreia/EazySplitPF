package br.com.eazysplit.pf


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.eazysplit.pf.adapters.RestaurantListAdapter
import br.com.eazysplit.pf.models.Restaurant
import br.com.eazysplit.pf.ui.RestaurantActivity
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = view.findViewById(R.id.rvListRestaurante) as RecyclerView

        val db = FirebaseFirestore.getInstance()

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
                    restaurant.id = doc.id
                    restaurantList.add(restaurant)
                }
            }

            recyclerView.layoutManager = LinearLayoutManager(activity)

            recyclerView.adapter = RestaurantListAdapter(activity!!, restaurantList) {
                val restaurantIntent = Intent(activity, RestaurantActivity::class.java)
                restaurantIntent.putExtra("RESTAURANT_ID", it.id)
                startActivity(restaurantIntent)
            }

            recyclerView.itemAnimator = DefaultItemAnimator()

        }

        return view
    }


}
