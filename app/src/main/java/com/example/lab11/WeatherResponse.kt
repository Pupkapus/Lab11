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