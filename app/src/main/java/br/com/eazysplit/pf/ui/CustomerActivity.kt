package br.com.eazysplit.pf.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.eazysplit.pf.R
import kotlinx.android.synthetic.main.activity_customer.*

class CustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        registerCustomer()
    }

    fun registerCustomer(){
        btRegister.setOnClickListener {

        }
    }
}
