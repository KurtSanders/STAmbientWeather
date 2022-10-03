# Ambient Weather® Station™ 
*Hubitat® Integration for Ambient Weather® Stations by SanderSoft™*
### Hubitat Version: 6.0.0

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/readme.png" width="50">[Change-log & Version Release Features](https://github.com/KurtSanders/STAmbientWeather/wiki/Features-by-Version)

### Install via [Hubitat Package Manager](https://hubitatpackagemanager.hubitatcommunity.com/), which automates all the below.

---

### Description:

A custom Hubitat® SmartApp and various device drivers which provides a connection to the weather data generated from your personal [Ambient Weather® station](https://www.ambientweather.com/ambientnet.html).  

This Hubitat® application provides access to your [Ambientweather.net](https://ambientweather.net/) weather data via the [AmbientWeather API](https://ambientweather.docs.apiary.io/#).  The user can set the polling rate of their weather data from either a manual or automatic refresh cycle (1 min to 180 mins (3 hours)).

### Supported Ambient Brand Devices:

* [Ambient Weather Station](https://ambientweather.com/ws-2902-smart-weather-station) with Internet Access
* [PM25](https://ambientweather.com/ampm25.html)/[AQIN](https://ambientweather.com/indoor-wireless-air-quality-monitor-aqin) Sensors
* Soil Sensors
* Wind Sensor
* Rain Sensor
* Up to 8 Weather Remote Sensors


### Requirements:
* You must create your own and have access to the following **private data strings** displayed at your [AWS Account](https://ambientweather.net/account)  and [My Devices](https://ambientweather.net/devices) webpages. 
    * [macAddress](https://ambientweather.net/devices)
![](https://aws1.discourse-cdn.com/smartthings/original/3X/c/0/c0dd23531f62ff196cee61b3f286e3d0bb5d0322.png)
    * [apiKey](https://ambientweather.net/account)
        
