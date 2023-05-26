<h2 align="center">  МИНИСТЕРСТВО НАУКИ И ВЫСШЕГО ОБРАЗОВАНИЯ РОССИЙСКОЙ ФЕДЕРАЦИИ ФЕДЕРАЛЬНОЕ ГОСУДАРСТВЕННОЕ БЮДЖЕТНОЕ ОБРАЗОВАТЕЛЬНОЕ УЧРЕЖДЕНИЕ ВЫСШЕГО ОБРАЗОВАНИЯ «САХАЛИНСКИЙ ГОСУДАРСТВЕННЫЙ УНИВЕРСИТЕТ» </h2>
<div align="center">
<h3>Институт естественных наук и техносферной безопасности
<br>
Кафедра информатики
<br>
Половников Владислав Олегович</h3>

<br>
<h3>Лабораторная работа №11
<br>
“Приложение погода”
<br>
01.03.02 Прикладная математика и информатика</h3>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<h3 align="right">Научный руководитель
<br>
Соболев Евгений Игоревич
</h3>

<h3 align="center">Южно-Сахалинск
<br>
2023г.
</h3>
<hr>
</div>
<p>
Kotlin - это высокоэффективный современный язык программирования, разработанный компанией JetBrains. Он имеет очень чёткий и лаконичный синтаксис, что делает ваш код лёгким для чтения. Kotlin широко используется во всём мире, и его популярность среди разработчиков постоянно растёт. Многие разработчики, использующие Kotlin, отмечают, что это делает их работу быстрее и увлекательнее.
</p>

<h3 align="center">Задание</h3>

1.	Реализовать приложение по макету, материалы для макета в архиве lab11.zip.
2.	Реализовать с помощью https://openweathermap.org/api, заполнение данными, город назначения выбирайте самостоятельно.


<h1 align="center">Решение</h2>

![img1](/2.png)

<h2 align="center">MainActivity</h2>

```kotlin
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
private var City_Name = "Yuzhno-Sakhalinsk"

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

        val response = apiService.getCurrentWeather(City_Name, API_KEY, "metric")
        response.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    val weatherData = WeatherData()
                    print(weatherResponse)
                    if (weatherResponse != null) {
                        weatherData.city = City_Name
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

```

<h2 align="center">WeatherData</h2>

```kotlin
package com.example.lab11

class WeatherData {
    var city = ""
    var dateUpdate = ""
    var statusWeather = ""
    var temperature =""
    var minTemperature = ""
    var maxTemperature = ""
    var sunriseTime = ""
    var sunsetTime = ""
    var windPower = ""
    var pressurePower = ""
    var humidityPower = ""
}
```

<h2 align="center">WeatherResponse</h2>

```kotlin
package com.example.lab11

data class WeatherResponse(
    val cityName: String?,
    val weather: List<Weather>?,
    val main: Main?,
    val sys: Sys?,
    val wind: Wind?
)

data class Weather(
    val main: String?,
    val description: String?
)

data class Main(
    val temp: Float?,
    val temp_min: Float?,
    val temp_max: Float?,
    val pressure: Float?,
    val humidity: Float?
)

data class Sys(
    val sunrise: Long?,
    val sunset: Long?
)

data class Wind(
    val speed: Float?
)

```

<h2 align="center">activity_main</h2>

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/background"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/city"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="City"
        android:layout_marginTop="10dp"
        android:textColor="@color/white"
        android:textSize="25dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/date_update"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Date update"
        android:textColor="@color/white"
        android:textSize="15dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/city" />

    <TextView
        android:id="@+id/status_weather"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="100dp"
        android:textSize="18dp"
        android:text="Status"
        android:textColor="@color/white"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/date_update" />

    <TextView
        android:id="@+id/temperature"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Temperature"
        android:textSize="70dp"
        android:layout_marginTop="10dp"
        android:textColor="@color/white"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/status_weather" />

    <LinearLayout
        android:id="@+id/min_max_temperature"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:orientation="horizontal"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/temperature">

        <TextView
            android:id="@+id/min_temperature"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="15dp"
            android:layout_marginRight="30dp"
            android:textColor="@color/white"
            android:layout_weight="1"
            android:text="Min temperature" />

        <TextView
            android:id="@+id/max_temperature"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="15dp"
            android:textColor="@color/white"
            android:layout_weight="1"
            android:text="Max temperature" />
    </LinearLayout>

    <LinearLayout
        android:id="@+id/layout_first_status"
        android:layout_width="match_parent"
        android:layout_marginHorizontal="20dp"
        android:layout_height="90dp"
        android:layout_marginBottom="10dp"
        android:orientation="horizontal"
        app:layout_constraintBottom_toTopOf="@+id/layout_second_status"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">


        <LinearLayout
            android:id="@+id/layout_sunrise"
            android:layout_width="90dp"
            android:layout_height="90dp"
            android:layout_marginRight="10dp"
            android:layout_weight="1"
            android:gravity="center"
            android:background="@color/grey"
            android:orientation="vertical">

            <ImageView
                android:id="@+id/image_sunrise"
                android:layout_width="30dp"
                android:layout_height="30dp"
                android:tint="@android:color/white"
                app:srcCompat="@drawable/sunrise" />

            <TextView
                android:id="@+id/hint_image_sunrise"
                android:layout_width="wrap_content"
                android:textColor="@color/white"
                android:textSize="13dp"
                android:layout_height="wrap_content"
                android:text="Sunrise" />

            <TextView
                android:id="@+id/sunrise_time"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="18dp"
                android:gravity="center"
                android:textColor="@color/white"
                android:text="Date" />
        </LinearLayout>

        <LinearLayout
            android:id="@+id/layout_sunset"
            android:layout_width="90dp"
            android:layout_height="90dp"
            android:layout_marginRight="10dp"
            android:layout_weight="1"
            android:background="@color/grey"
            android:gravity="center"
            android:orientation="vertical">

            <ImageView
                android:id="@+id/image_sunset"
                android:layout_width="30dp"
                android:layout_height="30dp"
                android:tint="@android:color/white"
                app:srcCompat="@drawable/sunset" />

            <TextView
                android:id="@+id/hint_image_sunset"
                android:layout_width="wrap_content"
                android:textColor="@color/white"
                android:textSize="13dp"
                android:layout_height="wrap_content"
                android:text="Sunset" />

            <TextView
                android:id="@+id/sunset_time"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="18dp"
                android:gravity="center"
                android:textColor="@color/white"
                android:text="Date" />
        </LinearLayout>

        <LinearLayout
            android:id="@+id/layout_wind"
            android:layout_width="90dp"
            android:layout_height="90dp"
            android:background="@color/grey"
            android:layout_weight="1"
            android:gravity="center"
            android:orientation="vertical">

            <ImageView
                android:id="@+id/image_wind"
                android:layout_width="30dp"
                android:layout_height="30dp"
                android:tint="@android:color/white"
                app:srcCompat="@drawable/wind" />

            <TextView
                android:id="@+id/hint_image_wind"
                android:layout_width="wrap_content"
                android:textColor="@color/white"
                android:textSize="13dp"
                android:layout_height="wrap_content"
                android:text="Wind" />

            <TextView
                android:id="@+id/wind_power"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="18dp"
                android:gravity="center"
                android:textColor="@color/white"
                android:text="Power" />
        </LinearLayout>
    </LinearLayout>

    <LinearLayout
        android:id="@+id/layout_second_status"
        android:layout_width="match_parent"
        android:layout_marginHorizontal="20dp"
        android:layout_height="90dp"
        android:orientation="horizontal"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginBottom="20dp">

        <LinearLayout
            android:id="@+id/layout_pressure"
            android:layout_width="90dp"
            android:layout_height="90dp"
            android:layout_marginRight="10dp"
            android:layout_weight="1"
            android:gravity="center"
            android:background="@color/grey"
            android:orientation="vertical">

            <ImageView
                android:id="@+id/image_pressure"
                android:layout_width="30dp"
                android:layout_height="30dp"
                android:tint="@android:color/white"
                app:srcCompat="@drawable/pressure" />

            <TextView
                android:id="@+id/hint_image_pressure"
                android:layout_width="wrap_content"
                android:textColor="@color/white"
                android:textSize="13dp"
                android:layout_height="wrap_content"
                android:text="Pressure" />

            <TextView
                android:id="@+id/pressure_power"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="18dp"
                android:gravity="center"
                android:textColor="@color/white"
                android:text="Power" />
        </LinearLayout>

        <LinearLayout
            android:id="@+id/layout_humidity"
            android:layout_width="90dp"
            android:layout_height="90dp"
            android:layout_marginRight="10dp"
            android:layout_weight="1"
            android:background="@color/grey"
            android:gravity="center"
            android:orientation="vertical">

            <ImageView
                android:id="@+id/image_humidity"
                android:layout_width="30dp"
                android:layout_height="30dp"
                android:tint="@android:color/white"
                app:srcCompat="@drawable/humidity" />

            <TextView
                android:id="@+id/hint_image_humidity"
                android:layout_width="wrap_content"
                android:textColor="@color/white"
                android:textSize="13dp"
                android:layout_height="wrap_content"
                android:text="humidity" />

            <TextView
                android:id="@+id/humidity_power"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="18dp"
                android:gravity="center"
                android:textColor="@color/white"
                android:text="Power" />
        </LinearLayout>

        <LinearLayout
            android:id="@+id/layout_created_by"
            android:layout_width="90dp"
            android:layout_height="90dp"
            android:background="@color/grey"
            android:layout_weight="1"
            android:gravity="center"
            android:orientation="vertical">

            <ImageView
                android:id="@+id/image_created_by"
                android:layout_width="30dp"
                android:layout_height="30dp"
                android:tint="@android:color/white"
                app:srcCompat="@drawable/info" />

            <TextView
                android:id="@+id/hint_image_created_by"
                android:layout_width="wrap_content"
                android:textColor="@color/white"
                android:textSize="13dp"
                android:layout_height="wrap_content"
                android:text="Created by" />

            <TextView
                android:id="@+id/created_by_name"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="18dp"
                android:gravity="center"
                android:textColor="@color/white"
                android:text="Vlad" />
        </LinearLayout>
    </LinearLayout>
</androidx.constraintlayout.widget.ConstraintLayout>
```

<h2 align="center">Вывод</h2>
Я научился создавать приложение погода и использовать API
