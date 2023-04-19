package dev.app.mobileapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val textView = findViewById<TextView>(R.id.welcomeTV)
        textView.animate().apply {
            alpha(1f).duration = 3000
            scaleX(1.1f).duration = 4000
            scaleY(1.1f).duration = 4000
        }.start()

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)


    }
}