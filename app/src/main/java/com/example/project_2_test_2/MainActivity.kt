package com.example.project_2_test_2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragmentOne -> {
                    Log.d("Navigation", "Fragment One selected")
                    replaceFragment(FragmentOne())
                    true
                }
                R.id.fragmentTwo -> {
                    Log.d("Navigation", "Fragment Two selected")
                    replaceFragment(FragmentTwo())
                    true
                }
                else -> false
            }
        }

        // Check if savedInstanceState is null to avoid recreating the fragment after orientation changes
        if (savedInstanceState == null) {
            // Set the initial fragment to be displayed, default is FragmentOne
            bottomNavigation.selectedItemId = R.id.fragmentOne
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }
}
