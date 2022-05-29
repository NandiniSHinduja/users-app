package com.example.usersapplication.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.usersapplication.MySingleton
import com.example.usersapplication.R
import com.example.usersapplication.adapters.UserItemClicked
import com.example.usersapplication.adapters.UsersAdapter
import com.example.usersapplication.models.UsersDetails
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject

class MainActivity : AppCompatActivity(), UserItemClicked {

    private lateinit var mAdapter: UsersAdapter
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerview = findViewById<RecyclerView>(R.id.UsersRV)
        recyclerview.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = UsersAdapter(this)
        recyclerview.adapter = mAdapter
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                val latitude = it.latitude
                val longitude = it.longitude
                val url =
                    "https://api.weatherbit.io/v2.0/forecast/daily?lat=" + latitude + "&lon=" + longitude + "&key=0be17abc16ae481284c71ad148d80130&units=M"
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    {
                        val usersJsonArray = it.getJSONArray("data")
                        val usersJsonObject = usersJsonArray.getJSONObject(0)
                        val temp = usersJsonObject.getDouble("temp")
                        val doubleTemp = temp.toInt()
                        val doubleText = doubleTemp.toString()
                        val weather: JSONObject = usersJsonObject.getJSONObject("weather")
                        val visual = weather.getString("icon")
                        val weather_url1 =
                            "https://www.weatherbit.io/static/img/icons/$visual.png"
                        val temperatureIV: ImageView = findViewById(R.id.weather_iv)
                        Glide.with(this@MainActivity).load(weather_url1).into(temperatureIV)
                        val temperature: TextView = findViewById(R.id.temperature)
                        temperature.setText(doubleText + "\u2103")

                    },
                    {

                    }
                )
                MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

            }
        }

    }


    private fun fetchData() {
        val url = "https://randomuser.me/api/?results=25"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val usersJsonArray = it.getJSONArray("results")
                val usersArray = ArrayList<UsersDetails>()
                for (i in 0 until usersJsonArray.length()) {
                    val usersJsonObject = usersJsonArray.getJSONObject(i)
                    val nameJSONObject = usersJsonObject.getJSONObject("name")
                    val imageJSONObject = usersJsonObject.getJSONObject("picture")
                    val dobJSONObject = usersJsonObject.getJSONObject("dob")
                    val locationJSONObject = usersJsonObject.getJSONObject("location")
                    val users = UsersDetails(
                        nameJSONObject.getString("first"),
                        nameJSONObject.getString("last"),
                        dobJSONObject.getString("age"),
                        imageJSONObject.getString("large"),
                        usersJsonObject.getString("gender"),
                        usersJsonObject.getString("email"),
                        locationJSONObject.getString("city")

                    )
                    usersArray.add(users)
                }

                mAdapter.updateUsers(usersArray)
            },
            {

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: UsersDetails) {

    }

//    override fun onItemClicked(item: News) {
//        val builder =  CustomTabsIntent.Builder()
//        val customTabsIntent = builder.build()
//        customTabsIntent.launchUrl(this, Uri.parse(item.url))
//    }
}
