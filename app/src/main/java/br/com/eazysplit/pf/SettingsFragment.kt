package br.com.eazysplit.pf

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.Button
import br.com.eazysplit.pf.ui.CardListActivity
import br.com.eazysplit.pf.ui.CustomerActivity
import br.com.eazysplit.pf.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val mAuth = FirebaseAuth.getInstance()

        val btEditCustomer = view.findViewById(R.id.btEditCustomer) as Button
        val btLanguage = view.findViewById(R.id.btLanguage) as Button
        val btPaymentMethod = view.findViewById(R.id.btPaymentMethod) as Button
        val btExit = view.findViewById(R.id.btExit) as Button

        btEditCustomer.setOnClickListener {
            val customerIntent = Intent(activity!!, CustomerActivity::class.java)
            startActivity(customerIntent)
        }

        btLanguage.setOnClickListener {

        }

        btPaymentMethod.setOnClickListener {
            val cardListIntent = Intent(activity!!, CardListActivity::class.java)
            startActivity(cardListIntent)
        }

        btExit.setOnClickListener {
            mAuth.signOut()
            val loginIntent = Intent(activity!!, LoginActivity::class.java)
            startActivity(loginIntent)
            activity!!.finish()
        }

        return view
    }

}