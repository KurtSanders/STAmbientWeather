# Ambient Weather® Station™ (AWS) 
*Hubitat® Integration for Ambient Weather® Stations by SanderSoft™*
### Hubitat AWS Suite Version: 7.0.0

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/readme.png" width="50">[Change-log & Version Release Features](https://github.com/KurtSanders/STAmbientWeather/wiki/Features-by-Version)

#### You must install [Hubitat Package Manager](https://hubitatpackagemanager.hubitatcommunity.com/) and install Ambient Weather Station application via HPM. Search in HPM for the keyword 'Weather'.

[Hubitat Community Support WebLink](https://community.hubitat.com/t/release-ambient-weather-station-app/128838) 

---

### Description:

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/HE%20AWS%20Dashboard.jpg" width="700"> 

A custom Hubitat® SmartApp integrates weather/environmental data generated from one or more personal [Ambient Weather® stations](https://www.ambientweather.com/ambientnet.html), sensors, and accessories.  

This Hubitat® application provides integration to your [Ambientweather.net](https://ambientweather.net/) weather data via the [AmbientWeather API](https://ambientweather.docs.apiary.io/#) using a single asynchronous poll (`asynchttpGet`) across all stations on your account. The HE user can set the polling rate of their weather and sensor data from either a manual or automatic refresh cycle (1 min to 180 mins (3 hours)).

### Multi-Station Architecture (v7.0.0+)

Version 7.0.0 supports multiple Ambient Weather stations from a **single API poll** using either (or a mix) of two modes—choose **one mode per station**:

* **Option A — Direct Multi-MAC (Shared Settings):** Select one or more stations directly in the main app. All stations share the same units of measure, data-key filter, and alert thresholds.
* **Option B — Per-Station Child Apps (Independent Settings):** Create an **Ambient Weather Station Child** app for any station that needs its own units of measure, data-key filter, or alert thresholds. Stations assigned to a Child App are automatically excluded from Option A (and vice versa).

```mermaid
flowchart TD
    API["Ambient Weather Cloud API<br/>/v1/devices (Single asynchttpGet Poll)"] --> Parent["Ambient Weather Station<br/>(Parent App v7.0.0)"]
    Parent -->|"Option A: Direct Multi-MAC<br/>(Shared Units, Keys & Alerts)"| DirectMAC1["Station 1 Devices<br/>Console + Remote(s) + PM<br/>(Legacy or MAC-Scoped DNI)"]
    Parent -->|"Option A: Direct Multi-MAC"| DirectMAC2["Station 2 Devices<br/>Console + Remote(s) + PM<br/>(MAC-Scoped DNI)"]
    Parent -->|"Option B: Dispatches by MAC"| Child1["Ambient Weather Station Child<br/>(Independent Units, Keys & Alerts)"]
    Child1 --> ChildDevs["Station N Devices<br/>Console + Remote(s) + PM<br/>(Child App MAC-Scoped DNI)"]
```

#### Upgrading from v6.x
* **Existing Single-Station Users (Option A):** Seamless upgrade—existing devices keep their legacy DNIs, so dashboards, Rule Machine rules, and sensor names stay intact.
* **Adding a 2nd Station:** Keep your original station in **Option A** and either add the new station in **Option A** or create a **Child App (Option B)** for it.
* **Moving an Existing Station to a Child App:** Because a Child App has its own `app.id`, moving an existing station out of Option A into a Child App creates new devices under the Child App (requiring rule/dashboard updates and removal of the old parent devices).

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
    * [Locate your weather station's MAC address](https://ambientweather.net/devices)
    * [Locate your Ambient API key](https://ambientweather.net/account)
