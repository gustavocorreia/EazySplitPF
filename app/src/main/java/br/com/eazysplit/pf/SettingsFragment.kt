package br.com.eazysplit.pf

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import br.com.eazysplit.pf.ui.CardListActivity
import br.com.eazysplit.pf.ui.CustomerActivity
import br.com.eazysplit.pf.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mAuth = FirebaseAuth.getInstance()


        btEditCustomer.setOnClickListener {
            val user = mAuth.currentUser
            val customerIntent = Intent(activity!!, CustomerActivity::class.java)
        }

        btLanguage.setOnClickListener {

        }

        btPaymentMethod.setOnClickListener {
            val cardListIntent = Intent(activity!!, CardListActivity::class.java)
            startActivity(cardListIntent)
            activity!!.finish()
        }

        btExit.setOnClickListener {
            mAuth.signOut()
            val loginIntent = Intent(activity!!, LoginActivity::class.java)
            startActivity(loginIntent)
            activity!!.finish()
        }
    }

}