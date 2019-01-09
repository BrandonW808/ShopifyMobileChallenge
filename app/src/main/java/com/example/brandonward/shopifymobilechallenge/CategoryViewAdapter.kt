package com.example.brandonward.shopifymobilechallenge

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_cell.view.*

class CategoryViewAdapter(val customCollectionFeed: CustomCollectionFeed) : RecyclerView.Adapter<CategoryViewHolder>() {

    //Number of Categories to View
    override fun getItemCount(): Int {
        return customCollectionFeed.custom_collections.count()
    }

    //When the view is created, inflate the category_cell layout into the parent
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val collection = inflater.inflate(R.layout.category_cell, parent, false)
        return CategoryViewHolder(collection)
    }

    //When the view loads, set up the proper variables
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var collection = customCollectionFeed.custom_collections[position]
        val collectionName = customCollectionFeed.custom_collections[position].title
        holder.view.categoryTextView.text = collectionName

        val categoryImageView = holder.view.categoryImageView

        //Third party image loader which caches the image
        Picasso.get().load(customCollectionFeed.custom_collections[position].image.src).into(categoryImageView)

        holder.collection = collection

    }



}

class CategoryViewHolder(val view: View, var collection: CustomCollection? = null): RecyclerView.ViewHolder(view) {

    //Objects to be passed to ProductActivity
    companion object {
        val COLLECTIONID: String = "collectionid"
        val COLLECTIONIMG: String = "collectionimg"
        val COLLECTIONNAME: String = "collectionname"
        val COLLECTIONBODY: String = "collectionbody"
    }

    init {
        //When each category_cell is clicked, create the intent and inject the companion objects into it
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