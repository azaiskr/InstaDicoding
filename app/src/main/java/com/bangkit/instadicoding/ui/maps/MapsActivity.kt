package com.bangkit.instadicoding.ui.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bangkit.instadicoding.R
import com.bangkit.instadicoding.databinding.ActivityMapsBinding
import com.bangkit.instadicoding.utils.State
import com.bangkit.instadicoding.utils.ViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewMode by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Maps View"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.rounded_arrow_back_ios_24)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
            isCompassEnabled = true
            isScrollGesturesEnabled = true
            isMapToolbarEnabled = true
            isMyLocationButtonEnabled = true
            isRotateGesturesEnabled = true
            isIndoorLevelPickerEnabled = true
        }

        setCustomMaps()
        showMarker()
        getMyLocation()
    }

    private fun showMarker() {
        viewMode.getStoriesLocation(1).observe(this) {
            when (it) {
                is State.Loading -> {

                }
                is State.Success -> {
                    Log.d("MapsActivity", "Number of stories: ${it.data.size}")
                    it.data.forEach { story ->
                        Log.d("MapsActivity", "Story: $story")
                        val latLng = story.lat?.let { lat ->
                            story.lon?.let { lon ->
                                LatLng(lat, lon)
                            }
                        }
                        latLng?.let {
                            mMap.addMarker(
                                MarkerOptions().position(latLng).title(story.name)
                                    .snippet(story.description)
                            )
                        }
                    }
                }
                is State.Error -> {
                    showToast(it.error)
                }
            }
        }
    }

    private fun setCustomMaps() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.custom_map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
                showToast("Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
            showToast("Can't find style. Error: $e")
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.maps_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }

            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }

            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }

            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }

            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun showToast(mssg: String?) {
        Toast.makeText(this, mssg, Toast.LENGTH_SHORT).show()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    companion object{
        private const val TAG = "MapsActivity"
    }

}