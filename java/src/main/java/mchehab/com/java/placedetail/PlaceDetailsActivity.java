package mchehab.com.java.placedetail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mchehab.com.java.R;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ViewPager viewPager;
    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;
    private LinearLayout linearLayout;

    private List<Fragment> listFragments = new ArrayList<>();
    private List<String> listTitles = new ArrayList<>();

    private String placeId = "";

    private final String API_KEY = "YOUR_API_KEY_HERE";
    private final String REQUEST = "https://maps.googleapis.com/maps/api/place/details/json?key=" + API_KEY;
    private final String PHOTO_REQUEST = "https://maps.googleapis.com/maps/api/place/photo?key=" + API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        linearLayout = findViewById(R.id.linearLayout);

        if (!getIntent().hasExtra("placeid")) {
            finish();
        }

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        supportMapFragment.getMapAsync(this);

        placeId = getIntent().getStringExtra("placeid");
        String url = REQUEST + "&placeid=" + placeId;
        new PlaceDetailsAsync().execute(url);

        listTitles.add("Reviews");
        listTitles.add("Photos");

        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void switchMap(int mapVisibility) {
        supportMapFragment.getView().setVisibility(mapVisibility);
        if (mapVisibility == View.VISIBLE) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mapListSwitcher) {
            if (item.getTitle().toString().equalsIgnoreCase("Map")) {
                switchMap(View.VISIBLE);
                item.setTitle("List");
            } else {
                switchMap(View.GONE);
                item.setTitle("Map");
            }
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private class PlaceDetailsAsync extends AsyncTask<String, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                return new JSONObject(stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                JSONObject result = jsonObject.getJSONObject("result");
                setActionBarTitle(result.getString("name"));
                animateGoogleMap(result);
                ArrayList<String> listReviews = getReviews(result);
                ArrayList<String> listPhotos = getPhotos(result);

                Bundle reviewsBundle = new Bundle();
                Bundle imageBundle = new Bundle();
                reviewsBundle.putStringArrayList("reviews", listReviews);
                imageBundle.putStringArrayList("photos", listPhotos);
                ImageFragment imageFragment = new ImageFragment();
                ReviewsFragment reviewsFragment = new ReviewsFragment();
                reviewsFragment.setArguments(reviewsBundle);
                imageFragment.setArguments(imageBundle);
                listFragments.add(reviewsFragment);
                listFragments.add(imageFragment);
                viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), listFragments, listTitles));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void animateGoogleMap(JSONObject jsonObject) {
            try {
                String name = jsonObject.getString("name");
                JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");
                LatLng latLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(name)
                        .position(latLng);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                googleMap.addMarker(markerOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private ArrayList<String> getReviews(JSONObject jsonObject) {
            ArrayList<String> reviewsList = new ArrayList<>();
            try {
                if (jsonObject.has("reviews")) {
                    JSONArray reviewsJSON = jsonObject.getJSONArray("reviews");
                    for (int i = 0; i < reviewsJSON.length(); i++) {
                        JSONObject reviewJSON = reviewsJSON.getJSONObject(i);
                        double rating = reviewJSON.getDouble("rating");
                        String text = reviewJSON.getString("text");
                        if (text.length() == 0) {
                            text = "Rating: " + rating + "/5";
                        } else {
                            text += "\nRating: " + rating + "/5";
                            ;
                        }
                        reviewsList.add(text);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return reviewsList;
        }

        private ArrayList<String> getPhotos(JSONObject jsonObject) {
            ArrayList<String> list = new ArrayList<>();
            try {
                if (jsonObject.has("photos")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("photos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        int height = json.getInt("height");
                        int width = json.getInt("width");
                        String photoReference = json.getString("photo_reference");
                        String url = PHOTO_REQUEST + "&photoreference=" + photoReference + "&maxwidth=" + width + "&maxheight=" + height;
                        list.add(url);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}