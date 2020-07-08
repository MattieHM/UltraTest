package com.example.lab_2_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_rating.*
import java.io.IOException

class Rating : AppCompatActivity(), ItemClick {

    //Передача клика по пользователю
    override fun onItemClick(pos: Int) {
        val userName = ratings[pos].login
        val intent = Intent(this, DetalRating::class.java)
        intent.putExtra("user", userName)
        startActivity(intent)
    }

    var ratings: ArrayList<Ratingg> = ArrayList()
    var sortP: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        addRating()
    }

    //Вывод всех результатов из базы рейтинга
    fun addRating() {

        val af = Observable.fromCallable {

            val db = TestDatabase.getAppDataBase(context = this)
            val ratingDao = db?.readoutDAO()
            ratingDao!!.getallResult

        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {

                if (it != null) {

                    for (item in it) {
                        ratings.add(Ratingg(item.login.toString(), item.result.toString()))
                    }
                    ratingRecyclerView.layoutManager = LinearLayoutManager(this)
                    ratingRecyclerView.adapter = RatingAdapter(ratings, this, this)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu2, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        val searchView =
            searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val text = ratings.binarySearchBy("$newText"){it.login}
                actionSE(text)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_sort -> {
                actionSO()
                return true
            }
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

    //Вывод результатов из базы рейтинга по поиску
  fun actionSE(poisk:Int) {
      if(poisk < 0){
          ratingRecyclerView.layoutManager = LinearLayoutManager(this)
          ratingRecyclerView.adapter = RatingAdapter(ratings, this, this)}
      else{
          var ratingsSE: ArrayList<Ratingg> = ArrayList()
          ratingsSE.add(Ratingg( ratings[poisk].login,  ratings[poisk].percent))
          ratingRecyclerView.layoutManager = LinearLayoutManager(this)
          ratingRecyclerView.adapter = RatingAdapter(ratingsSE, this, this)
      }
    }

    //Сортировка
    fun actionSO() {

        if(sortP==0){
            Toast.makeText(getApplicationContext(), "Отсортировано по логину", Toast.LENGTH_SHORT).show();
            ratings.sortBy {it.login}
            sortP=1
        }
        else {
            Toast.makeText(getApplicationContext(), "Отсортировано по проценту", Toast.LENGTH_SHORT).show();
            ratings.sortBy {it.percent}
            sortP=0
        }

        ratingRecyclerView.layoutManager = LinearLayoutManager(this)
        ratingRecyclerView.adapter = RatingAdapter(ratings, this, this)
    }

    fun about() {
        val intent = Intent(this, About::class.java)
        startActivity(intent)
    }

    fun newtest() {
        if(isReallyOnline2()){ val intent = Intent(this, Test::class.java)
            startActivity(intent)}
    }

    fun logout() {
        finish()
    }
}

fun isReallyOnline2(): Boolean {
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