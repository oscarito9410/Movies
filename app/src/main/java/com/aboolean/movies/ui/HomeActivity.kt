package com.aboolean.movies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.aboolean.movies.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as? NavHostFragment)?.let { nav ->
            NavigationUI.setupWithNavController(bottomNavigationView, nav.navController)
        }
    }
}
