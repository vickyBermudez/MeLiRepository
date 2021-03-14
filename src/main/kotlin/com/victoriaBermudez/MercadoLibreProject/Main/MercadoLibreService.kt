package com.victoriaBermudez.MercadoLibreProject.Main
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MercadoLibreService {
    @GET("item_listing_presentation")
    fun getAllCars(@Query("list_length") listLength: Int): Call<ItemListing>
}