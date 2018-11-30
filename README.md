This is a simple demo that shows how to use the Place details API.  
There are two modules:  
* app: Kotlin implementation
* java: Java implementation

The code in this repo builds on top of the Places APi demo [here](https://github.com/MChehab94/Google-Maps-Places-Demo). Nearby places are displayed to the user by clustering the markers. 
Upon selecting a place, the user will be redirected to a details page displaying place-specific details such as reviews and photos.  

A thorough explanation can be found in the post [here](http://mobiledevhub.com/2018/11/30/android-how-to-use-place-details-api/).  
NOTE: In order to use this code, you need to create an API key for Google Maps from https://console.developers.google.com and enable Places API. 
Once you generate the key, you need to add it in **google_maps_api_key.xml** located **res -> values**
