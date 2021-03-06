package com.example.app_dtt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_house_profile.*

/**
 * Class creating the profile activity of a particular house
 */
class HouseProfile : AppCompatActivity(), OnMapReadyCallback {
    // variable containing the google map for the activity
    private lateinit var map: GoogleMap

    // variables for the latitude and longitude of the house for use once the map is ready
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    // the base url of the DTT house API
    private val baseUrl = "https://intern.docker-dev.d-tt.nl"

    /**
     * Creates the activity view, retrieving the data about its house and assigning it to the separate UI components
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_profile)

        val i = intent
        val houseItem: HouseItem = i.getSerializableExtra("houseItem") as HouseItem
        setUpProfile(houseItem)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Sets up the different UI components with the houseItem data
     * @param houseItem Contains the data of the house
     */
    fun setUpProfile(houseItem: HouseItem) {
        val priceString = getString(R.string.price_string, houseItem.price)
        val distanceString = getString(R.string.distance_string, houseItem.distance)
        loadImage(houseProfileImage, houseItem.image)

        priceProfile.text = priceString
        bedsTv.text = houseItem.bedrooms.toString()
        bathsTv.text = houseItem.bathrooms.toString()
        sizeTv.text = houseItem.size.toString()
        distanceTv.text = distanceString
        longitude = houseItem.longitude.toDouble()
        latitude = houseItem.latitude.toDouble()
        descriptioTv.text = houseItem.description
    }

    /**
     * Loads an image with the given url into imageView using Glide
     * @param imageView The ImageView to load the image into
     * @param url End of the url to retrieve the image from
     */
    fun loadImage(imageView: ImageView, url: String) {
        Glide.with(this).load(baseUrl + url).into(imageView)
    }

    /**
     * Finishes the activity when called
     * @param view
     */
    fun onClickButton(view: View) {
        finish()
    }

    /**
     * Sets up the google map once it is ready. Setting up the marker for the house
     * @param googleMap The google map to modify
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
        val markerLongLat = LatLng(latitude, longitude)
        val zoom = 10f
        map.addMarker(MarkerOptions().position(markerLongLat).title("Home"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLongLat, zoom))
        map.uiSettings.setAllGesturesEnabled(false)

        map.setOnMapClickListener {
            val gmmIntentUri =
                Uri.parse("geo:0,0?q=$latitude,$longitude(Home)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }


}