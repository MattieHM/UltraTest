package com.example.lab_2_3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home_admin.*
import kotlinx.android.synthetic.main.activity_test.*

class HomeAdmin : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        Observable.fromCallable {

            val db = TestDatabase.getAppDataBase(context = this)
            val userDao = db?.readoutDAO()
            userDao!!.allReadoutItems
        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {

                var usersT: Int = 0
                if (it != null)

                    for (item in it) {
                        ++usersT
                    }
                users.setText(usersT.toString())
            }

        Observable.fromCallable {

            val db = TestDatabase.getAppDataBase(context = this)
            val userDao = db?.readoutDAO()
            userDao!!.getallResult
        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {

                var testsT: Int = 0
                var testsnoT: Int = 0
                if (it != null)

                    for (item in it) {
                        if(item.result == "100%") {
                        ++testsT}
                        else {
                            ++testsnoT
                        }
                    }
                tests.setText(testsT.toString())
                testsno.setText(testsnoT.toString())
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuad, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.relog -> {
                relog()
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

    fun relog() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun logout() {
        finish()
    }
}
