# Ambient Weather® Station™ (AWS) 
*Hubitat® Integration for Ambient Weather® Stations by SanderSoft™*
### Hubitat AWS Suite Version: 6.3.1

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/readme.png" width="50">[Change-log & Version Release Features](https://github.com/KurtSanders/STAmbientWeather/wiki/Features-by-Version)

#### You must install [Hubitat Package Manager](https://hubitatpackagemanager.hubitatcommunity.com/) and install Ambient Weather Station application via HPM. Search in HPM for keyword 'Weather'.

[Hubitat Community Support WebLink](https://community.hubitat.com/t/release-ambient-weather-station-app/128838) 

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

#### AWS Settings Examples
* Preference Screens

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/Ambient Preferences Settings.jpg" width="700"> 

* Import Selection 
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/Ambient Weather Station Data Key Import Selector.jpg" width="700"> 



#### Ambient Weather Station Device Information :arrow_right: Hubitat™

[View Ambient Weather Station's Device Data Specs](https://github.com/ambient-weather/api-docs/wiki/Device-Data-Specs)

| HE Capability | HE Attribute | Reported Type |
|:------------:|:-------------------|:-------------------:|
|capability|Battery|Battery|
|capability|IlluminanceMeasurement|Illuminance|
|capability|Refresh|Refresh|
|capability|RelativeHumidityMeasurement|Humidity|
|capability|Sensor| N/A |
|capability|TemperatureMeasurement|Temperature|
|capability|UltravioletIndex|Ultraviolet|

| HE Attribute | HE Device State Data Key | Reported Type |
|:------------:|:-------------------|:-------------------:|
|attribute|baromabsin_display|string|
|attribute|baromabsin|number|
|attribute|baromrelin_display|string|
|attribute|baromrelin|number|
|attribute|batt_lightning|number|
|attribute|city|string|
|attribute|dailyrainin_display|string|
|attribute|dailyrainin|number|
|attribute|date|string|
|attribute|date|string|
|attribute|dateutc|string|
|attribute|dewPoint_display|string|
|attribute|dewpoint|number|
|attribute|dewPoint|number|
|attribute|eventrainin_display|string|
|attribute|eventrainin|number|
|attribute|feelsLike_display|string|
|attribute|feelslike|number|
|attribute|feelsLike|number|
|attribute|hourlyrainin_display|string|
|attribute|hourlyrainin|number|
|attribute|humidity_display|string|
|attribute|humidityin_display|string|
|attribute|humidityin|number|
|attribute|lastRain|string|
|attribute|lastRainDuration|string|
|attribute|lastSTupdate|string|
|attribute|lightning_day|number|
|attribute|lightning_distance|number|
|attribute|lightning_hour|number|
|attribute|lightning_time|number|
|attribute|location|string|
|attribute|macAddress|string|
|attribute|maxdailygust_display|string|
|attribute|maxdailygust|number|
|attribute|monthlyrainin_display|string|
|attribute|monthlyrainin|number|
|attribute|pwsName|string|
|attribute|scheduleFreqMin|string|
|attribute|solarradiation_display|string|
|attribute|solarradiation|number|
|attribute|tempf_display|string|
|attribute|tempinf_display|string|
|attribute|tempinf|number|
|attribute|totalrainin_display|string|
|attribute|totalrainin|number|
|attribute|ultravioletIndexDisplay|string|
|attribute|version|string|
|attribute|weeklyrainin_display|string|
|attribute|weeklyrainin|number|
|attribute|wind_cardinal|string|
|attribute|wind|number|
|attribute|winddir|string|
|attribute|windDirection|number|
|attribute|winddirection|string|
|attribute|windgustmph_display|string|
|attribute|windgustmph|number|
|attribute|windSpeed|number|
|attribute|windspeedmph_display|string|
|attribute|windspeedmph|number|
|attribute|windVector|string|


### Requirements:
* You must create your own and have access to the following **private data strings** displayed at your [AWS Account](https://ambientweather.net/account)  and [My Devices](https://ambientweather.net/devices) webpages. 
    * [Locate your weather station's macAddress](https://ambientweather.net/devices)
    * [Locate your Ambient API key](https://ambientweather.net/account)
        
