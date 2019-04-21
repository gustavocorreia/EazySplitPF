package br.com.eazysplit.pf.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import br.com.eazysplit.pf.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME = 900L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        carregar()
    }

    fun carregar(){
        Handler().postDelayed({
            val nextScreenIntent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(nextScreenIntent)
            finish()
        }, SPLASH_TIME)
    }
}
