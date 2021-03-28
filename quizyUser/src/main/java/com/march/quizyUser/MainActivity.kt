package com.march.quizyUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.march.quizyHost.R
import com.march.quizyUser.quizzes.QuizzesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, QuizzesFragment())
            .commit()
    }
}