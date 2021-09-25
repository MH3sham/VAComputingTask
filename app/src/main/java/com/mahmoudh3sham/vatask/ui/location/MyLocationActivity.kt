package com.mahmoudh3sham.vatask.ui.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.mahmoudh3sham.vatask.R
import com.mahmoudh3sham.vatask.databinding.ActivityMyLocationBinding
import com.mahmoudh3sham.vatask.di.component.ActivityComponent
import com.mahmoudh3sham.vatask.ui.base.BaseActivity
import com.mahmoudh3sham.vatask.utils.GpsUtils
import com.mahmoudh3sham.vatask.utils.LocationUtils
import com.mahmoudh3sham.vatask.utils.NetworkUtils
import io.nlopez.smartlocation.SmartLocation
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MyLocationActivity : BaseActivity<MyLocationViewModel>(), MyLocationNavigator, OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks {

    val REQUEST_LOCATION_CODE = 1

    lateinit var binding: ActivityMyLocationBinding
    private lateinit var map: GoogleMap
    private lateinit var mLatLng: LatLng

    lateinit var adMap: HashMap<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel.setNavigator(this)

        binding.mMapView.onCreate(savedInstanceState)
        binding.mMapView.getMapAsync(this)

        setUp()

        if(!NetworkUtils.isNetworkConnected(this)){
            showErrorMessage("Network Connection Needed To Load Map Locations")
        }
    }

    private fun setUp() {
        binding.toolbar.backBtn.setOnClickListener { finish() }

        binding.currentLocationBtn.setOnClickListener {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.location_rationale),
                REQUEST_LOCATION_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        gpsCheck()
    }

    private fun gpsCheck() {
        if (!LocationUtils.isGPSEnabled(this)) {
            GpsUtils(this).turnGPSOn { isGPSEnable -> }
        }
    }

    private fun handleMyLocationClick() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (EasyPermissions.somePermissionPermanentlyDenied(
                    this,
                    listOf(Manifest.permission.ACCESS_FINE_LOCATION)
                )
            ) {
                AppSettingsDialog.Builder(this).build().show()
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.location_rationale),
                    REQUEST_LOCATION_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        } else {

            map.clear()
            if (LocationUtils.isGPSEnabled(this)) {
                SmartLocation.with(this).location().oneFix().start { location: Location ->
                    mLatLng = LatLng(location.latitude, location.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15f))
                    binding.lPin.visibility = View.VISIBLE
                }
            } else {
                mLatLng = map.cameraPosition.target
                showErrorMessage(getString(R.string.location_rationale))
            }
        }
    }


    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, MyLocationActivity::class.java)
        }
    }

    override fun performDependencyInjection(buildComponent: ActivityComponent?) {
        buildComponent?.inject(this)
    }

    override fun onResume() {
        binding.mMapView.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mMapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        handleMyLocationClick()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        handleMyLocationClick()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showErrorMessage(getString(R.string.location_rationale))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen
            if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                handleMyLocationClick()
            }
        }
    }

}
