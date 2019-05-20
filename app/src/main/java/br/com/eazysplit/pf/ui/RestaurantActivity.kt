package br.com.eazysplit.pf.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.eazysplit.pf.R
import br.com.eazysplit.pf.models.Restaurant
import br.com.eazysplit.pf.util.PermissionUtils
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_restaurant.*

class RestaurantActivity : AppCompatActivity() ,OnMapReadyCallback{

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDB: FirebaseFirestore

    lateinit var mFragmentMap: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private var restaurantID: String? = null


    fun initLocationListener() {
        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location?) {
                val minhalocalizacao = LatLng(location?.latitude!!, location.longitude)

                mMap.addMarker(MarkerOptions().position(minhalocalizacao))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minhalocalizacao, 15f))
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        }
    }

    override fun onStart() {
        super.onStart()

        mAuth = FirebaseAuth.getInstance()
        mDB = FirebaseFirestore.getInstance()

        mDB.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true).build()

        mFragmentMap = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment



        if (mAuth.currentUser == null) {
            val loginIntent = Intent(this@RestaurantActivity, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        } else {
            loadRestaurant()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        PermissionUtils.validarPremissoes(
            listOf(Manifest.permission.ACCESS_FINE_LOCATION),
            this, 1
        )
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0, 0f, locationListener
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissao in grantResults) {
            if (permissao == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            } else {
                requestLocationUpdates()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        initLocationListener()
        requestLocationUpdates()
        mMap.setOnMapClickListener {
            mMap.addMarker(MarkerOptions().position(it))
        }
        mMap.setOnMapLongClickListener {
            mMap.addMarker(MarkerOptions().position(it))
        }

        // Add a marker in Sydney and move the camera
        val fiaPaulista = LatLng(-23.5565804, -46.662113)
        mMap.addMarker(
            MarkerOptions().position(fiaPaulista).title("Fiap - Campus Paulista")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiaPaulista, 15f))
        //  adicionarFormaCicular(fiaPaulista)

    }
    private fun adicionarFormaCicular(latLng: LatLng){
        val circulo = CircleOptions()
        circulo.center(latLng)
        circulo.radius(200.0)
        circulo.fillColor(Color.argb(128,0,51,102))
        circulo.strokeColor(Color.argb(128,0,51,102))
        mMap.addCircle(circulo)
    }

    private fun loadRestaurant() {
        restaurantID = intent.extras.getString("RESTAURANT_ID")


        restaurantID?.let {
            val restaurantDocument = mDB.collection("restaurants").document(it)

            restaurantDocument.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val restaurant = it.result?.toObject(Restaurant::class.java)
                    tvRating.text = "avaliação" + restaurant!!.rating.toString() + "/10"
                    tvRestaurantTitle.text = restaurant.name
                    tvDescription.text = restaurant.description
                    //var geo = restaurant.geolocalization

                    Glide.with(this)
                        .load(restaurant.url_image)
                        .into(ivRestaurantMap)

                } else {
                    Log.e("RestaurantActivity", it.exception?.message)
                }
            }
        }


    }


    private fun SupportMapFragment.getMapAsync(restaurantActivity: RestaurantActivity) {

    }
}
