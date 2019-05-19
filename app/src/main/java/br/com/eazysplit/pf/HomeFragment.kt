package br.com.eazysplit.pf


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.eazysplit.pf.adapters.RestaurantListAdapter
import br.com.eazysplit.pf.models.Restaurant
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
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
                    restaurantList.add(restaurant)
                }
            }

            // comentário teste

            recyclerView.layoutManager = LinearLayoutManager(activity)

            recyclerView.adapter = RestaurantListAdapter(activity!!, restaurantList) {
                Toast.makeText(activity!!, it.name, Toast.LENGTH_LONG).show()
            }

            recyclerView.itemAnimator = DefaultItemAnimator()

        }

        return view
    }


}
