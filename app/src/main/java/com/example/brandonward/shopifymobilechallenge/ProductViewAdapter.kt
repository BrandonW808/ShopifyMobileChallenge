package com.example.brandonward.shopifymobilechallenge

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_cell.view.*
import kotlinx.android.synthetic.main.product_cell.view.*

class ProductViewAdapter(val customProductFeed: CustomProductFeed, val categoryName: String, val categoryImgSrc: String) : RecyclerView.Adapter<CategoryViewHolder>() {

    //Number of Categories to View
    override fun getItemCount(): Int {
        println("FEED: " + customProductFeed + " COLLECTS: " + customProductFeed.products)
        if (customProductFeed.products != null) {
            return customProductFeed.products.count()
        }else{
            return 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val productCell = inflater.inflate(R.layout.product_cell, parent, false)
        return CategoryViewHolder(productCell)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val productName = customProductFeed.products.get(position).title
//        val productImgSrc = customProductFeed.custom_products.get(position).image.src
        holder?.view?.productTitle.text = productName
        var variant_num: Int = 0
        for (variant in customProductFeed.products[position].variants){
            variant_num += variant.inventory_quantity
        }
        holder?.view?.productInventory.text = "Qty: " + variant_num

        holder?.view?.CollectionTitle.text = categoryName

        val categoryImageView = holder.view.productCollectionImage

        Picasso.get().load(categoryImgSrc).into(categoryImageView)

    }



}

class ProductViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener{
            val intent = Intent(view.context, MainActivity::class.java)
        }
    }
}