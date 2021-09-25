package com.mahmoudh3sham.vatask.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mahmoudh3sham.vatask.R
import java.math.BigDecimal
import java.util.*

object LocationUtils {

    fun getAddress(lat: Double, lng: Double, context: Context): String {
        var address: String
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addresses = geocoder.getFromLocation(lat, lng, 1)
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            address = addresses[0].getAddressLine(0)
        } catch (e: Exception) {
            e.printStackTrace()
            address = context.getText(R.string.no_address_found) as String
        }
        return address
    }


    fun getDistance(
        firstLat: Double,
        firstLng: Double,
        secondLat: Double,
        secondLng: Double
    ): Float {
        val fLocation = Location("FirstLocation")
        fLocation.latitude = firstLat
        fLocation.longitude = firstLng
        val sLocation = Location("SecondLocation")
        sLocation.latitude = secondLat
        sLocation.longitude = secondLng
        val distance =
            fLocation.distanceTo(sLocation) / 1000 //returns distance in kilos (remove "/1000" >>> Meter)
        return round(distance, 2)
    }

    private fun round(d: Float, decimalPlace: Int): Float {
        var bd = BigDecimal(d.toString())
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
        val dis = bd.toString()
        return java.lang.Float.valueOf(dis)
    }


    fun goToMapsDirections(lat: String, lng: String, context: Context) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    fun goToMaps(lat: String, lng: String, context: Context) {
        val uri = "http://maps.google.com/maps?q=loc:$lat,$lng"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
    }

    fun checkLocationPermission(activity: Activity?, REQUEST_LOCATION_CODE: Int): Boolean {
        return if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            }
            false
        } else true
    }

    fun isGPSEnabled(activity: Activity): Boolean {
        val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ignored: Exception) {
        }
        return gpsEnabled
    }

}