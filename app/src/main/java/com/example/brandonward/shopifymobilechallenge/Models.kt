package com.example.brandonward.shopifymobilechallenge

import org.json.JSONArray

class CustomCollectionFeed(val custom_collections: List<CustomCollection>)

class CustomCollection(val id: Long, val title: String, val image: CustomCollectionImage, val body_html: String)

class CustomCollectionImage(val src: String)

class CustomProductCollectFeed(val collects: List<CustomProductCollect>)

class CustomProductCollect(val product_id: Long)

class CustomProductFeed(val products: List<CustomProduct>)

class CustomProduct(val title: String, val id: Long, val variants: List<CustomProductVariant>)

class CustomProductVariant(val inventory_quantity: Int)

class CustomProductImage(val src: String)
