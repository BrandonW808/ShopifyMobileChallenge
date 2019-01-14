package com.example.brandonward.shopifymobilechallenge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_failed.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val TAG: String? = MainActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectingTextView.visibility = View.VISIBLE
        loadingBar.visibility = View.VISIBLE

        //Set up our layout manager for the RecyclerView in activity_main
        categoryView.layoutManager = LinearLayoutManager(this)

        //Get the data from the provided URL
        fetchJSONData()

    }

    fun fetchJSONData(){
        val dataURL = resources.getString(R.string.url)

        //Build the Request and a client to make the request
        val request = Request.Builder().url(dataURL).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            //Notify user if unable to connect to store
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, resources.getString(R.string.error) + e)
                runOnUiThread{
                    setContentView(R.layout.activity_failed)

                }

            }

            //Once a response is gained, parse into the categories and update the UI through the CategoryViewAdapter
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    connectingTextView.visibility = View.GONE
                    loadingBar.visibility = View.GONE
                }


                val  body = response.body()?.string()

                val parser = GsonBuilder().create()

                val categoryFeed = parser.fromJson(body, CustomCollectionFeed::class.java)

                //For debugging, uncomment below line to view JSON received
                //println(body)
                runOnUiThread {  categoryView.adapter = CollectionViewAdapter(categoryFeed) }
            }
        })

    }
}
