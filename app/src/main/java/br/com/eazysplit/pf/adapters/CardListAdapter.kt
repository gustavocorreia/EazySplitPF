package br.com.eazysplit.pf.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.eazysplit.pf.models.Card

class CardListAdapter(
    private val context: Context,
    private val cards: List<Card>,
    private val listener: (Card) -> Unit
) : RecyclerView.Adapter<CardListAdapter.CardViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CardViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(cardViewHolder: CardViewHolder, i: Int) {
        cardViewHolder.bindView(cards[i], listener)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(card: Card, listener: (Card) -> Unit) = with(itemView){

        }
    }
}