package com.example.brandonward.shopifymobilechallenge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_failed.*
import kotlinx.android.synthetic.main.activity_product.*
import okhttp3.*
import java.io.IOException

class ProductActivity : AppCompatActivity() {
    val TAG: String? = ProductActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        productLoadingBar.visibility = View.VISIBLE
        connectingProductsTextView.visibility = View.VISIBLE

        val collectionCardImageView= findViewById(R.id.collectionCardImageView) as ImageView

        //Set up out layout manager
        productView.layoutManager = LinearLayoutManager(this)

        //Grab the variables passed from CategoryViewAdapter
        val bundle: Bundle? = intent.extras
        var collectionid: Long = bundle!!.getLong(CollectionViewHolder.COLLECTIONID)
        var collectionName: String = bundle!!.getString(CollectionViewHolder.COLLECTIONNAME)
        var collectionImgSrc: String = bundle!!.getString(CollectionViewHolder.COLLECTIONIMG)
        var collectionBody: String = bundle!!.getString(CollectionViewHolder.COLLECTIONBODY)
        Picasso.get().load(collectionImgSrc).into(collectionCardImageView)
        val collectionCardTitle = findViewById(R.id.collectionCardTitleTextView) as TextView
        val collectionCardBody = findViewById(R.id.collectionCardBodyTextView) as TextView
        collectionCardTitle.text = collectionName
        collectionCardBodyTextView.text = collectionBody

        //Fetch the list of product ids
        fetchJSONIDData(collectionid, collectionName, collectionImgSrc)

    }

    fun fetchJSONIDData(categoryid: Long, collectionName: String, collectionImgSrc: String){

        val dataURL = resources.getString(R.string.categoryidurl1) + categoryid + resources.getString(R.string.categoryidurl2)

        //Set up the request and client
        val request = Request.Builder().url(dataURL).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, resources.getString(R.string.error) + e)
                runOnUiThread{
                    setContentView(R.layout.activity_failed)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                val parser = GsonBuilder().create()

                //Parse the JSON into the CustomerProductCollectFeed (as seen in Models.kt)
                val productIDs: CustomProductCollectFeed = parser.fromJson(body, CustomProductCollectFeed::class.java)

                //Fetch the data for all products
                fetchJSONData(productIDs, collectionName, collectionImgSrc)
            }
        })

    }

    fun fetchJSONData(productIDs: CustomProductCollectFeed, collectionName: String, collectionImgSrc: String){
        //Build the list of product IDs separated by commas and inject it into the URL
        var idString = ""
        for (i in 0..productIDs.collects.count()-1){
            idString += productIDs.collects[i].product_id
            if (i != productIDs.collects.count()-1){
                idString += ","
            }
        }
        val newDataURL = resources.getString(R.string.idurl1) + idString + resources.getString(R.string.idurl2)

        //Set up the request and client
        val newrequest = Request.Builder().url(newDataURL).build()
        val secondclient = OkHttpClient()

        secondclient.newCall(newrequest).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, resources.getString(R.string.error) + e)
                runOnUiThread{
                    setContentView(R.layout.activity_failed)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    productLoadingBar.visibility = View.GONE
                    connectingProductsTextView.visibility = View.GONE
                }


                val body = response.body()?.string()

                val parser = GsonBuilder().create()

                //Parse the JSON into the CustomerProductFeed (As seen in Models.kt)
                val productFeed: CustomProductFeed = parser.fromJson(body, CustomProductFeed::class.java)

                //Update the Product List Page
                runOnUiThread {  productView.adapter = ProductViewAdapter(productFeed, collectionName,collectionImgSrc) }
                println(body)

            }
        })
    }
}
