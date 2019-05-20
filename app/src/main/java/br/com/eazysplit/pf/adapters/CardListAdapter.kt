package br.com.eazysplit.pf.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.Card
import kotlinx.android.synthetic.main.card_row.view.*

class CardListAdapter(
    private val context: Context,
    private val cards: MutableList<Card>,
    private val listener: (Card) -> Unit,
    private val delListener: (Card) -> Unit
) : RecyclerView.Adapter<CardListAdapter.CardViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_row, viewGroup, false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(cardViewHolder: CardViewHolder, i: Int) {
        cardViewHolder.bindView(cards[i], listener, delListener)
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(card: Card, listener: (Card) -> Unit, delListener: (Card) -> Unit) = with(itemView){
            tvCVC.text = card.codeValidate.toString()
            tvName.text = card.name
            tvNumber.text = card.number
            tvExpiration.text = card.monthValidate.toString().padStart(2, '0') + "/" + card.yearValidate.toString()

            setOnClickListener{
                listener(card)
                notifyItemChanged(this@CardViewHolder.position);
                notifyDataSetChanged();
            }

            btDelete.setOnClickListener {
                delListener(card)
                cards.remove(card)

            }

        }


    }

}