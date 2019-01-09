package com.example.brandonward.shopifymobilechallenge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val mScrollPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up our layout manager for the RecyclerView in activity_main
        categoryView.layoutManager = LinearLayoutManager(this)

        //Get the data from the provided URL
        fetchJSONData()

    }

    fun fetchJSONData(){
        println("Fetching JSON data...")
        val dataURL = "https://shopicruit.myshopify.com/admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6"

        //Build the Request and a client to make the request
        val request = Request.Builder().url(dataURL).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            //Notify user if unable to connect to store
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(applicationContext, "UNABLE TO CONNECT TO STORE", Toast.LENGTH_LONG)
            }

            //Once a response is gained, parse into the categories and update the UI through the CategoryViewAdapter
            override fun onResponse(call: Call, response: Response) {
                val  body = response.body()?.string()

                val parser = GsonBuilder().create()

                val categoryFeed = parser.fromJson(body, CustomCollectionFeed::class.java)

                //For debugging, uncomment below line to view JSON received
                //println(body)
                runOnUiThread {  categoryView.adapter = CategoryViewAdapter(categoryFeed) }
            }
        })

    }
}
