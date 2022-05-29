package com.example.usersapplication.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.usersapplication.MySingleton
import com.example.usersapplication.R
import org.json.JSONObject

class UserDetailsActivity : AppCompatActivity() {
    private var firstNameTV: TextView? = null
    private var lastNameTV: TextView? = null
    private var ageTV: TextView? = null
    private var genderTV: TextView? = null
    private var mailTV: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_screen)
        initViews()
        fetchData()
    }

    private fun fetchData() {
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val age = intent.getStringExtra("age")
        val gender = intent.getStringExtra("gender")
        val email = intent.getStringExtra("email")
        val imageUrl = intent.getStringExtra("imageUrl")
        val city = intent.getStringExtra("city")
        firstNameTV!!.setText(firstName)
        lastNameTV?.setText(lastName)
        ageTV?.setText(age)
        genderTV?.setText(gender)
        mailTV?.setText(email)
        val image: ImageView = findViewById(R.id.largeImage)
        Glide.with(this@UserDetailsActivity).load(imageUrl).into(image)
        val url =
            "https://api.weatherbit.io/v2.0/forecast/daily?city=" + city + "&key=0be17abc16ae481284c71ad148d80130&units=M"
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
                Glide.with(this@UserDetailsActivity).load(weather_url1).into(temperatureIV)
                val temperature: TextView = findViewById(R.id.temp)
                temperature.setText(doubleText + "\u2103")

            },
            {

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    private fun initViews() {
        lastNameTV = findViewById<TextView>(R.id.lastName)
        firstNameTV = findViewById<TextView>(R.id.firstName)
        ageTV = findViewById<TextView>(R.id.age)
        genderTV = findViewById<TextView>(R.id.gender)
        mailTV = findViewById<TextView>(R.id.email)
    }

}