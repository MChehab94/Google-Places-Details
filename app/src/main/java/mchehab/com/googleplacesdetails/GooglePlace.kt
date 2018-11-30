package mchehab.com.googleplacesdetails

import com.google.android.gms.maps.model.LatLng

data class GooglePlace(val name: String, val latLng: LatLng, val placeId: String){
    override fun toString(): String {
        return name
    }
}