# Ambient Weather® Station™ 
*Hubitat® Integration for Ambient Weather® Stations by SanderSoft™*
### Hubitat Version: 6.1.0

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/readme.png" width="50">[Change-log & Version Release Features](https://github.com/KurtSanders/STAmbientWeather/wiki/Features-by-Version)

#### You must install [Hubitat Package Manager](https://hubitatpackagemanager.hubitatcommunity.com/) and install Ambient Weather Station application via HPM. Search in HPM for keyword 'Weather'.

---

### Description:

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/HE%20AWS%20Dashboard.jpg" width="700"> 

A custom Hubitat® SmartApp which provides integration to weather/environmental data generated from one's personal [Ambient Weather® station](https://www.ambientweather.com/ambientnet.html), sensors  and accessories.  

This Hubitat® application provides integration to your [Ambientweather.net](https://ambientweather.net/) weather data via the [AmbientWeather API](https://ambientweather.docs.apiary.io/#).  The HE user can set the polling rate of their weather and sensor data from either a manual or automatic refresh cycle (1 min to 180 mins (3 hours)).

### Supported Ambient Weather Station Brand Devices and Accessories:

* [Ambient Weather Station](https://ambientweather.com/ws-2902-smart-weather-station) with Internet Access
* [PM25](https://ambientweather.com/ampm25.html)/[AQIN](https://ambientweather.com/indoor-wireless-air-quality-monitor-aqin) Sensors
* Soil Sensors
* Wind Sensor
* Rain Sensor
* Up to 8 Weather Remote Sensors

#### Ambient Weather Station :arrow_right: Hubitat™

| Capability :ballot_box_with_check: | Ambient Weather® Attribute | Show Decimals | Label|
|------------|-------------------|:-------------------:|-------------------|
| Energy | Max Daily Gust, mph | :ballot_box_with_check: |Wind Gust Daily Max|
| Power  | Wind Speed, mph | :ballot_box_with_check: |Wind Speed |
| Illuminance Measurement | Solar Radiation, lux, fc or w/m² | |Light|
| Motion Sensor¹ | windspeedmph > 1 = active | |Wind State |
| Relative Humidity Measurement | Outside Relative Humidity, % | |Humidity |
| Temperature Measurement | Outside Temperature, °F |:ballot_box_with_check:|Temperature|
| Ultraviolet Index | Ultraviolet Index, uv | | UVI |
| Water Sensor¹ | hourlyrainin > 0 = wet | | Rain Detected |

¹ It is STRONGLY recommended that one specifically designate the "motion sensors" and/or “leak detectors” in one’s Smart Home Alarm app to be notified rather than use the “Use every Motion or Moisture sensor” to avoid false alarms fro your Ambient Weather Station which uses these standard capabilities for weather conditions.



### Requirements:
* You must create your own and have access to the following **private data strings** displayed at your [AWS Account](https://ambientweather.net/account)  and [My Devices](https://ambientweather.net/devices) webpages. 
    * [macAddress](https://ambientweather.net/devices)
![](https://aws1.discourse-cdn.com/smartthings/original/3X/c/0/c0dd23531f62ff196cee61b3f286e3d0bb5d0322.png)
    * [apiKey](https://ambientweather.net/account)
        
