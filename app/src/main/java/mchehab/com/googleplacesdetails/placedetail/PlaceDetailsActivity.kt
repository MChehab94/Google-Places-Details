package mchehab.com.googleplacesdetails.placedetail

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_place_details.*
import mchehab.com.googleplacesdetails.R
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class PlaceDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment

    private val API_KEY = "YOUR_API_KEY_HERE"
    private val REQUEST = "https://maps.googleapis.com/maps/api/place/details/json?key=$API_KEY"
    private val PHOTO_REQUEST = "https://maps.googleapis.com/maps/api/place/photo?key=$API_KEY"

    private val imageFragment = ImageFragment()
    private val reviewsFragment = ReviewsFragment()
    private val listFragments = listOf(reviewsFragment, imageFragment)
    private val listTitles = listOf("Reviews", "Images")

    private var placeId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        tabLayout.setupWithViewPager(viewPager)

        //this should never be true
        if (intent.hasExtra("placeId")){
            placeId = intent.getStringExtra("placeId")

        }else{
            //show alert dialog and finish activity
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Cannot retrieve place, please try again later")
                .setPositiveButton("Ok") { dialog, which ->
                    dialog.dismiss()
                    finish()
                }
                .create()
                .show()
        }

        supportMapFragment = supportFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.place_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mapListSwitcher){
            if (item.title.toString().equals("List", true)){
                switchMap(View.GONE)
                item.title = "Map"
            }else{
                switchMap(View.VISIBLE)
                item.title = "List"
            }
        }else if (item?.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val url = "$REQUEST&placeid=$placeId"
        PlaceDetailsAsync().execute(url)
    }

    private fun switchMap(mapVisibility: Int){
        supportMapFragment.view?.visibility = mapVisibility
        if (mapVisibility == View.VISIBLE){
            linearLayout.visibility = View.GONE
        }else{
            linearLayout.visibility = View.VISIBLE
        }
    }

    private inner class PlaceDetailsAsync : AsyncTask<String, Int, JSONObject>() {
        override fun doInBackground(vararg params: String): JSONObject {
            val url = URL(params[0])
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.connect()
            val placeDetails = httpURLConnection.inputStream.bufferedReader().readText()
            return JSONObject(placeDetails)
        }

        override fun onPostExecute(jsonObject: JSONObject) {
            try {
                super.onPostExecute(jsonObject)
                val result = jsonObject.getJSONObject("result")
                setupActionBar(result.getString("name"))
                animateGoogleMap(result)
                val reviewsList = getReviews(result)
                val urls = getPhotos(result)
                val reviewsBundle = Bundle()
                val imageBundle = Bundle()
                reviewsBundle.putStringArrayList("reviews", reviewsList)
                imageBundle.putStringArrayList("photos", urls)
                reviewsFragment.arguments = reviewsBundle
                imageFragment.arguments = imageBundle
                viewPager.adapter = ViewPagerAdapter(
                    supportFragmentManager,
                    listFragments,
                    listTitles
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        private fun setupActionBar(title: String){
            supportActionBar?.title = title
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        private fun animateGoogleMap(jsonObject: JSONObject){
            val name = jsonObject.getString("name")
            val location = jsonObject.getJSONObject("geometry").getJSONObject("location")
            val latLng = LatLng(location.getDouble("lat"), location.getDouble("lng"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f))
            val marker = MarkerOptions()
                    .title(name)
                    .position(latLng)
            googleMap.addMarker(marker)
        }

        private fun getReviews(jsonObject: JSONObject): ArrayList<String>{
            val reviewsList = ArrayList<String>()
            if(jsonObject.has("reviews")){
                val reviews = jsonObject.getJSONArray("reviews")
                for (i in 0 until reviews.length()){
                    val reviewJSON = reviews.getJSONObject(i)
                    var text = reviewJSON.getString("text")
                    val rating = reviewJSON.getDouble("rating")
                    if (text.isEmpty()){
                        text = "Rating: $rating/5"
                    }else{
                        text = "$text\nRating: $rating/5"
                    }
                    reviewsList.add(text)
                }
            }

            return reviewsList
        }

        private fun getPhotos(jsonObject: JSONObject): ArrayList<String>{
            val urls = ArrayList<String>()
            if (jsonObject.has("photos")){
                val photos = jsonObject.getJSONArray("photos")
                for (i in 0 until photos.length()) {
                    val json = photos.getJSONObject(i)
                    val height = json.getInt("height")
                    val width = json.getInt("width")
                    val photoReference = json.get("photo_reference")
                    val url = "$PHOTO_REQUEST&photoreference=$photoReference&maxwidth=$width&maxheight=$height"
                    urls.add(url)
                }
            }
            return urls
        }
    }
}