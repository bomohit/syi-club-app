package com.example.clubapplication

import android.os.Bundle
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.clubapplication.viewmodel.loginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var username : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        username = intent.getStringExtra("username").toString()
        var viewModel = ViewModelProvider(this).get(loginViewModel::class.java)
        viewModel.id = "bomohit"
        username = "bomohit"
//        viewModel.id = username

        if (viewModel.id == "admin") {
            setContentView(R.layout.admin_activity_main)
        } else {
            setContentView(R.layout.activity_main)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (viewModel.id == "admin") {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_admin)
            val navView: NavigationView = findViewById(R.id.nav_view_admin)
            val navController = findNavController(R.id.nav_host_fragment_admin)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_admin_home, R.id.nav_admin_news), drawerLayout)
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
        else {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_logout), drawerLayout)
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {

        var navController: NavController = if (username == "admin") {
            findNavController(R.id.nav_host_fragment_admin)
        } else {
            findNavController(R.id.nav_host_fragment)
        }
        findViewById<TextView>(R.id.nav_name).text = username

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}