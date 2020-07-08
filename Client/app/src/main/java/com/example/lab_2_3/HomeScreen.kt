package com.example.lab_2_3

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException


class HomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    fun Test(view: View) {
       if(isReallyOnline()){val intent = Intent(this, Test::class.java)
            startActivity(intent)}
        else {
           Toast.makeText(getApplicationContext(), "Интернет соединение отсутствует!!!", Toast.LENGTH_SHORT).show();}
    }

    fun Rating(view: View) {
        if(isReallyOnline()){val intent = Intent(this, Rating::class.java)
        startActivity(intent)}
        else {Toast.makeText(getApplicationContext(), "Интернет соединение отсутствует!!!", Toast.LENGTH_SHORT).show();}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.gotest -> {
                genres()
                return true
            }
            R.id.about -> {
                about()
                return true
            }
            R.id.logout -> {
                logout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    fun about() {
        val intent = Intent(this, About::class.java)
        startActivity(intent)
    }

    fun genres() {
        val intent = Intent(this, Test::class.java)
        startActivity(intent)
    }

    fun logout() {
        finish()
    }

}

fun isReallyOnline(): Boolean {
    val runtime = Runtime.getRuntime()
    try {
        val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
        val exitValue = ipProcess.waitFor()
        return exitValue == 0
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    return false
}