package com.example.lab11

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.math.roundToInt


private var API_KEY = "843597ba3f0639b489332812f90e1fb6"
private var cityName = "Yuzhno-Sakhalinsk"

class MainActivity : AppCompatActivity() {
    private lateinit var city: TextView
    private lateinit var dateUpdate: TextView
    private lateinit var statusWeather: TextView
    private lateinit var temperature: TextView
    private lateinit var minTemperature: TextView
    private lateinit var maxTemperature: TextView
    private lateinit var sunriseTime: TextView
    private lateinit var sunsetTime: TextView
    private lateinit var windPower: TextView
    private lateinit var pressurePower: TextView
    private lateinit var humidityPower: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        city = findViewById(R.id.city)
        dateUpdate = findViewById(R.id.date_update)
        statusWeather = findViewById(R.id.status_weather)
        temperature = findViewById(R.id.temperature)
        minTemperature = findViewById(R.id.min_temperature)
        maxTemperature = findViewById(R.id.max_temperature)
        sunriseTime = findViewById(R.id.sunrise_time)
        sunsetTime = findViewById(R.id.sunset_time)
        windPower = findViewById(R.id.wind_power)
        pressurePower = findViewById(R.id.pressure_power)
        humidityPower = findViewById(R.id.humidity_power)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val response = apiService.getCurrentWeather(cityName, API_KEY, "metric")
        response.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    val weatherData = WeatherData()
                    print(weatherResponse)
                    if (weatherResponse != null) {
                        weatherData.city = cityName
                        weatherData.dateUpdate = currentDate()
                        weatherData.statusWeather = weatherResponse.weather?.get(0)?.main ?: ""
                        weatherData.temperature = weatherResponse.main?.temp?.roundToInt().toString()
                        weatherData.minTemperature = weatherResponse.main?.temp_min?.roundToInt().toString()
                        weatherData.maxTemperature = weatherResponse.main?.temp_max?.roundToInt().toString()
                        weatherData.sunriseTime = formatTime(weatherResponse.sys?.sunrise)
                        weatherData.sunsetTime = formatTime(weatherResponse.sys?.sunset)
                        weatherData.windPower = weatherResponse.wind?.speed?.roundToInt().toString()
                        weatherData.pressurePower = weatherResponse.main?.pressure?.roundToInt().toString()
                        weatherData.humidityPower = weatherResponse.main?.humidity?.roundToInt().toString()
                    }
                    UpdateUI(weatherData)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
            }
        })
    }

    fun currentDate(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        return formattedDate
    }

    fun formatTime(timestamp: Long?): String {
        val date = timestamp?.let { Date(it) }
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = date?.let { dateFormat.format(it) } ?: ""
        return formattedTime
    }

    fun UpdateUI(weatherData: WeatherData){
        city.setText(weatherData.city)
        dateUpdate.setText("Updated at: " + weatherData.dateUpdate)
        statusWeather.setText(weatherData.statusWeather)
        temperature.setText(weatherData.temperature+"°C")
        minTemperature.setText("Min Temp: " + weatherData.minTemperature + "°C")
        maxTemperature.setText("Max Temp: " + weatherData.maxTemperature + "°C")
        sunriseTime.setText(weatherData.sunriseTime)
        sunsetTime.setText(weatherData.sunsetTime)
        windPower.setText(weatherData.windPower)
        pressurePower.setText(weatherData.pressurePower)
        humidityPower.setText(weatherData.humidityPower)
    }

    interface ApiService {
        @GET("weather")
        fun getCurrentWeather(
            @Query("q") cityName: String,
            @Query("appid") apiKey: String,
            @Query("units") units: String
        ): Call<WeatherResponse>
    }

}