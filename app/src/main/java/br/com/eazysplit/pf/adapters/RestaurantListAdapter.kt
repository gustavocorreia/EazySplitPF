package br.com.eazysplit.pf.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.eazysplit.pf.models.Restaurant
import kotlinx.android.synthetic.main.restaurante_list.view.*

class RestaurantListAdapter(
    private val context: Context,
    private val restaurants: List<Restaurant>,
    private val listener: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RestaurantViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(restaurantViewHolder: RestaurantViewHolder, i: Int) {
        restaurantViewHolder.bindView(restaurants[i], listener)
    }

    class RestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(restaurant: Restaurant, listener: (Restaurant) -> Unit) = with(itemView){

        }
    }
}