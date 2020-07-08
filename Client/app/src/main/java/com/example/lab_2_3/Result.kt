package com.example.lab_2_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_result.*
import java.io.IOException
import kotlin.Result

class Result : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val truetest = intent.getIntExtra("truetest", 0)

        if (truetest >= 7) {
            Result.setText("Тест ПРОЙДЕН!!!")
        } else {
            Result.setText("Тест Не пройден... Гоу ЕЩЁ!!!")
        }
        Percent.setText("${truetest * 10}%")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.gotest -> {
                newtest()
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

    fun newtest() {
        if(isReallyOnline()){ val intent = Intent(this, Test::class.java)
        startActivity(intent)}
        else
        {Toast.makeText(getApplicationContext(), "Интернет соединение отсутствует!!!", Toast.LENGTH_SHORT).show();}
    }

    fun logout() {
        finish()
    }
}

