package mchehab.com.googleplacesdetails.location

import android.location.Location

interface LocationResultListener {
    fun getLocation(location: Location)
}