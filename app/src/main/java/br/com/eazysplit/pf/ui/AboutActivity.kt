package br.com.eazysplit.pf.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.eazysplit.pf.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
