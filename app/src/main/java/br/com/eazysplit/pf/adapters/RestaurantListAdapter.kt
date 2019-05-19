package br.com.eazysplit.pf.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.Restaurant
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.restaurant_row.view.*
import kotlinx.android.synthetic.main.restaurante_row.view.*
import kotlinx.android.synthetic.main.restaurante_row.view.ivRestaurant

class RestaurantListAdapter(
    private val context: Context,
    private val restaurants: List<Restaurant>,
    private val listener: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.restaurant_row, viewGroup, false)

        return RestaurantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(restaurantViewHolder: RestaurantViewHolder, i: Int) {
        restaurantViewHolder.bindView(restaurants[i], listener)
    }

    class RestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindView(restaurant: Restaurant, listener: (Restaurant) -> Unit) = with(itemView){
            tvName.text = restaurant.name
            tvDescription.text = restaurant.description

            Glide.with(context)
                .load(restaurant.url_image)
                .into(ivRestaurant)

            setOnClickListener {
                listener(restaurant)
            }

        }
    }
}