package com.example.brandonward.shopifymobilechallenge

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.collection_cell.view.*

class CollectionViewAdapter(val customCollectionFeed: CustomCollectionFeed) : RecyclerView.Adapter<CollectionViewHolder>() {

    //Number of Categories to View
    override fun getItemCount(): Int {
        return customCollectionFeed.custom_collections.count()
    }

    //When the view is created, inflate the collection_cell layout into the parent
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val collection = inflater.inflate(R.layout.collection_cell, parent, false)
        return CollectionViewHolder(collection)
    }

    //When the view loads, set up the proper variables
    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        var collection = customCollectionFeed.custom_collections[position]
        val collectionName = customCollectionFeed.custom_collections[position].title
        holder.view.collectionTextView.text = collectionName

        val collectionImageView = holder.view.collectionImageView

        //Third party image loader which caches the image
        Picasso.get().load(customCollectionFeed.custom_collections[position].image.src).into(collectionImageView)

        holder.collection = collection

    }



}

class CollectionViewHolder(val view: View, var collection: CustomCollection? = null): RecyclerView.ViewHolder(view) {

    //Objects to be passed to ProductActivity
    companion object {
        val COLLECTIONID: String = "collectionid"
        val COLLECTIONIMG: String = "collectionimg"
        val COLLECTIONNAME: String = "collectionname"
        val COLLECTIONBODY: String = "collectionbody"
    }

    init {
        //When each collection_cell is clicked, create the intent and inject the companion objects into it
        view.setOnClickListener{
            val intent = Intent(view.context, ProductActivity::class.java)
            intent.putExtra(COLLECTIONID, collection?.id)
            intent.putExtra(COLLECTIONIMG, collection?.image?.src)
            intent.putExtra(COLLECTIONNAME, collection?.title)
            intent.putExtra(COLLECTIONBODY, collection?.body_html)
            val keys = intent.extras.keySet()
            for (key in keys){
                val bundle: Bundle? = intent.extras
            }

            view.context.startActivity(intent)
        }
    }
}