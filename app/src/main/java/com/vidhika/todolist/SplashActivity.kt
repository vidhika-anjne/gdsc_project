package com.vidhika.todolist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.vidhika.todolist.MainActivity
import com.vidhika.todolist.R

import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val account = GoogleSignIn.getLastSignedInAccount(this)
//        if (account != null) {
////             User is signed in, navigate to MainActivity
//
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//
//        } else {
//            // User is not signed in, navigate to GoogleSignInActivity
//            val intent = Intent(this, google_signin::class.java)
//            startActivity(intent)
//        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (account != null) {
                // User is signed in, navigate to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // User is not signed in, navigate to GoogleSignInActivity
                val intent = Intent(this, google_signin::class.java)
                startActivity(intent)
            }
            finish()  // Optionally finish the splash screen activity
        }, 2000) // 2 seconds delay

//        finish() // Close SplashActivity
    }
}
