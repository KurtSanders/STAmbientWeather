# Ambient Weather® Station™ 
*SmartThings® & Hubitat™ Integration for Ambient Weather® Stations by SanderSoft™*
### Version: 4.3.0 (SmartThings™ Production Master Branch)
### Version: 5.0.1 (Hubitat™ Master Branch)
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/readme.png" width="50">[Change-log & Version Release Features](https://github.com/KurtSanders/STAmbientWeather/wiki/Features-by-Version)

---

#### :new: Updates 
1. Added **[Pushover™ Service](https://pushover.net/)** as an additional means for event notifications, along with SMS and ST Push. Pushover™ makes it easy to get real-time notifications on your Android, iPhone, iPad, and Desktop (Android Wear and Apple Watch, too!)
2. Added Hubitat™ compatible apps and drivers for alpha code testing.  Please review the [install instructions](https://github.com/KurtSanders/STAmbientWeather#hubitat-installation) and [known issues](https://github.com/KurtSanders/STAmbientWeather#known-issues) for the Hubitat™ platform.

### Description:

A custom SmartThings® SmartApp and Device Handlers (DTH) which provides a connection to the weather data generated from your personal [Ambient Weather® station](https://www.ambientweather.com/ambientnet.html).  

This SmartThings® application provides access to your [Ambientweather.net](https://ambientweather.net/) weather data via the [AmbientWeather API](https://ambientweather.docs.apiary.io/#).  The user can set the SmartThings Tile update/refresh rate of their weather data from either a manual or automatic refresh cycle (1 min to 180 mins (3 hours)).

## Weather Station Tile and Details View

### Multiple Ambient Weather® Station Instances of Independent Operating Nodes

<p align="center">
Allows for multiple Ambient Weather® system models or geolocation instances<br>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V4-MobileClient2.PNG" width=300>
</p>

### Screenshots of Device Attributes

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V4-MobileClient3.PNG" width=200>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V4-MobileClient4.PNG" width=200>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V4-MobileClient5.PNG" width=200>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V4-MobileClient6.PNG" width=200>
</p>

### Ambient Weather® Station with multiple remote temp/soil sensors

<p align="center"><img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V4-MobileClient9.PNG" width=200>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V303-MobileClient5.PNG" width=200>
</p>

### Ambient Weather® Station with Particle Monitor PM25
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/PM25-2.jpg" width=200>
</p>

### Weather Event SMS Alerts

Select the following events to send Weather SMS Text Alerts for user defined values:

   * Outside Low Temperature¹ 
   * Outside High Temperature¹
   * Rain Detected¹ 
   * Severe Weather (TWC® Alerts for Zipcode or lat/long coordinates) 

   ¹Alerts are hidden if the installed weather station does not provide a value.

	Weather SMS Alerts are sent per user preferences for once every {1,2,4,6,12,24} hours. 

<p align="center"><img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/Weather Event Alerts.PNG" width=300>
</p>

#### Filtering/Suppressing Selected Weather Alerts

One can filter/suppress selected Weather Alerts from the Ambient Weather Station's preferences options menu by selecting one or more of the listed [Nation Weather Services' Product Identifiers](https://forecast.weather.gov/product_types.php).   The default option (nothing selected) allows all Weather Alerts to be reported for the Zipcode or lat/long coordinates entered.  


### Requirements:

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/readme.png" width="50"> **YOU DO NOT NEED AN AMBIENT DEVELOPERS APP KEY**

* Your Ambient Weather® Station **API Key** (Required)
	* An Ambient Weather® Station **API Key** CAN BE SELF GENERATED FROM YOUR [AMBIENT DASHBOARD ACCOUNT VIEW](https://dashboard.ambientweather.net/account). The API key is used to securely connect this SmartThings application to your personal Ambient Weather® station data. Just locate your Ambient API key from your Ambient Weather® station account.

### Enter your Ambient API key in the ST IDE Editor for the SmartApp in the Settings section.

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-APIKey1.jpg" width="500"><br><br>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-APIKey2.jpg" width="400">
</p>

1. A personal [Ambient Weather® Station](https://www.ambientweather.com/ambientnet.html) which connects to the Ambient Weather® Network: (e.g., Model 2902A for example) and optionally up to 99 Ambient remote temperature/hydro sensor(s).
2. SmartThings Hub
3. Supported mobile device with **ST Legacy Client**. *This app will not work in the new Samsung SmartThings App*. 
4. A working knowledge of the SmartThings IDE
	* Installing a SmartApp & DTH from a GitHub repository (see [SmartThings GitHub IDE integration documentation](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github) for example instructions and use the Repository Owner, Name and Branch from installation instructions below)

		 
## Installation & Configuration

**GitHub Repository Integration**

Create a new SmartThings Repository entry in your SmartThings IDE under 'Settings' with the following values:

| Owner | Name | Branch |
|------|:-------:|--------|
| kurtsanders | STAmbientWeather | master |

**Required Files in your SmartThings IDE Repository**
These files below are required for V4.3.0 to operate.  You will need to Update from Repo

| IDE Repository    | Filename | Status |
| :---: | :----------| :---:  |
| My SmartApps      | kurtsanders : Ambient Weather Station | **Updated** |
| My Device Handler | kurtsanders : Ambient Particulate Monitor | UnChanged  |
| My Device Handler | kurtsanders : Ambient Weather Station | UnChanged |
| My Device Handler | kurtsanders : SmartWeather Tile Station | UnChanged |
| My Device Handler | kurtsanders : Ambient Weather Station Remote Sensor | UnChanged |

> - *It is strongly recommended that all previous versions/files of Ambient Weather Station be removed from your ST IDE and only the V4 files from the above table are listed in your SmartThings IDE.*
> - Note: V3 cannot be updated to V4 due to the extensive re-coding to accomodate multiple instances of Ambient Weather® Station nodes and SMS alerts.  A fresh/clean install of Ambient Weather Station is only supported.

**Instructions**

1. Using the 'Update from REPO' button in the 'My SmartApps' SmartThings IDE, check the 'Ambient Weather Station' SmartApp and publish & press Save.  
2. Using the 'Update from REPO' button in the "My Device Handlers" SmartThings IDE, check all three Ambient Weather DTH's listed above.  ([See GitHub IDE integration](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github)) from this STAmbientWeather(master) repository to your SmartThings IDE.
2. **Required Next Step before setup on mobile client:** You must edit the newly added 'Ambient Weather Station' SmartApp in the IDE SmartApps browser Tab 'App Settings' and enter your apiString (Your Ambient API Key).  Update/Save your changes.  *Known Issue: If you receive an error "Unauthorized" message trying to save/update the settings page for your API key, you might need to use the Incognito mode of Chrome or private settings of your browser to update this page.*
3. Locate the Ambient Weather Station app in the MarketPlace/SmartApps/My Apps list and click to launch the smartapp.
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V4-MobileClient1.PNG" width=300>
</p>

4. Update the following fields from Ambient Weather Station Preferences 
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V4-MobileClient8.PNG" width=300>
</p>

* Preferences
	- Zipcode or lat/long coordinates for Weather Forecast Info
	- Run Weather Station Refresh ('0' is 'Off/Manual Refresh')
	- 	(# mins for the APP to update weather values from your weather station)
	- Select Solar Radiation Units of Measure
	- Weather Alerts/Notifications >
	- Base Prefix Name
	- This SmartApp's Name (Used to name this App Instance in your Automation List)
	- IDE Logging (Optional)
		- debugVerbose	
		- infoVerbose	bool	
		-  Weather Underground API Calls
5. If you have Ambient remote temperature/hydro sensors attached to your Ambient Weather® network station, you will be presented with a "remote sensor" screen in which you **must provide** a short descriptive name for each using only alpha or numeric characters.  The remote sensors will be numbered on the screen according to their dip switch setting.
6. Display the new SmartThings Tile in your ST Mobile Client
7. It is recommended that one create a ST Room to organize the created weather devices.
8. Add the created weather devices to WebCore and any other smartApps to create weather related events.
9. **Important:** Please verify and deactivate in SmartThings Smart Home Monitor (SHM) the use of the wildcard settings, "All Leak Detectors and All Motion Sensors' for SHM actions.  Ambient Weather Station SmartAPP uses 'Leak Detection' sensor and 'Motion' sensor for Rain and Wind respectively. If you use the wildcard settings, you may get unexpected results for SHM when rain or wind is detected.   

## ActionTiles™ and STAmbientWeather

### Ambient Weather® Station ActionTiles™ Integration
<p align="center">
ActionTiles™
</p>
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTilesWeatherTile.jpg" width="250"><br>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTilesWeatherTile2.jpg" width="550">

<br>Ambient Weather Station ActionTiles™ Integration</p>

1. A STAmbientWeather APP preference option is available to "Create a SmartWeather Station Tile for use as an Ambient Weather® ActionTiles™ Weather Tile?".  
  * When this new preference option is set to 'ON', and after SAVING from the APP, a local "special use" version of the **SmartWeather Station Tile** child device will be created with the title _"Ambient SmartWeather Station"_.  It will only receive the required weather data fields from the Ambient Weather Station at the same update frequency as the STAmbientWeather APP refresh setting.
  * Please refer to the instructions on ActionTiles™ website on how to create a SmartWeather Station Weather Tile on your dashboard.      
  * If one has already installed the SmartThings namespace SmartWeather Tile Station installed and reporting their local weather, the child device created by STAmbientWeather will not impact that existing device.  
  * If one does not have SmartThings namespace SmartWeather Station Tile, you may choose to install and configure it separately so that your ActionTiles™ dashboard has both Ambient and local weather. 
     * [ActionTiles™ Tips & Tricks: How do I install a SmartWeather StationTile](https://www.smarttiles.click/help/tips-tricks/#Weather_Tile) 
  * Do not modify the kurtsanders namespace SmartWeather Station Tile DTH or device as it is pre-configured to work only with STAmbientWeather.

### Ambient Special Attributes

* Only a subset of Ambient weather attributes can be viewed from [ActionTiles™](https://www.actiontiles.com/). Per ActionTiles™ website, ActionTiles™ **only** [supports Things](https://support.actiontiles.com/knowledge-bases/8/articles/3556-compatible-smartthings-capabilities-device-type-abstractions) that are fully compliant with a standard Capability (device type abstraction) as defined by SmartThings Capabilities. 
* SmartThings publishes a [partial list](https://www.smartthings.com/products) of "WWST" (Works with SmartThings) Certified Devices but not all of those are compatible with ActionTiles, because some of them claim a "Capability" that ActionTiles™ has not built a Tile Type for.
* Units of measure on ActionTiles™ cannot be modified

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTilesColorTiles.jpg">
</p>

* One will need to edit the tile settings each of the three ActionTiles™ title headers to show decimals and custom  labels as shown below. 

#### Ambient Weather Station :arrow_right: ActionTiles™

| ActionTiles™ Thing :ballot_box_with_check: | Ambient Weather® Attribute | Show Decimals | Recommended Custom Label|
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

#### ActionTiles™ Things Panel

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTilesV3Things.jpg" width="600">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTilesWeatherTileSettings.jpg" width="300">
</p>

</p>

## WebCore Tiles Dashboard

You can import this WebCore piston template into your WebCore using the backup code: **6dj8**.  Remember to: 

1. Change the name to your SmartThings weather station name. 
2. Use ‘Tiles’ mode in the webCore browser settings for categories to display this piston as tiles!

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/WebCore0.jpeg" width="600">

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/WebCore1.PNG" width="600">
</p>

</p>


## Capabilities, Attributes & Commands
The following device capabilities, attributes and commands are available for your own subscriptions from WebCore(tm) or a custom smartApp.

        capability "Illuminance Measurement"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Refresh"
        capability "Water Sensor"
        capability "Ultraviolet Index"
        capability "Battery"

        // Wind Motion Detection
        capability "Motion Sensor"
        // Wind Speed Psuedo Capability
        capability "Power Meter"
        capability "Energy Meter"

        // Start of Ambient Weather API Rest MAP
        // Actual numeric values from Ambient Weather API non rounded"
        attribute "windspeedmph_real", "number"
        attribute "windgustmph_real", "number"
        attribute "maxdailygust_real", "number"
        attribute "tempf_real", "number"
        attribute "hourlyrainin_real", "number"
        attribute "eventrainin_real", "number"
        attribute "dailyrainin_real", "number"
        attribute "weeklyrainin_real", "number"
        attribute "monthlyrainin_real", "number"
        attribute "totalrainin_real", "number"
        attribute "baromrelin_real", "number"
        attribute "baromabsin_real", "number"
        attribute "humidity_real", "number"
        attribute "tempinf_real", "number"
        attribute "humidityin_real", "number"
        attribute "solarradiation_real", "number"
        attribute "feelsLike_real", "number"
        attribute "dewPoint_real", "number"

		// Numeric values from Ambient API are rounded to 0.1 if 0 < X < 0.1 
		// because SmartThings Tiles cannot display values 
		// less than 0.1 and greater than zero
        attribute "baromabsin", "string"
        attribute "baromrelin", "string"
        attribute "city", "string"
        attribute "dailyrainin", "string"
        attribute "date", "string"
        attribute "dateutc", "string"
        attribute "dewPoint", "string"
        attribute "dewpoint", "string"
        attribute "eventrainin", "string"
        attribute "feelsLike", "string"
        attribute "feelslike", "string"
        attribute "hourlyrainin", "string"
        attribute "humidity", "string"
        attribute "humidityin", "string"
        attribute "lastRain", "string"
        attribute "location", "string"
        attribute "lastRainDuration", "string"
        attribute "macAddress", "string"
        attribute "maxdailygust", "string"
        attribute "monthlyrainin", "string"
        attribute "pwsName", "string"
        attribute "solarradiation", "string"
        attribute "temperature", "string"
        attribute "tempinf", "string"
        attribute "totalrainin", "string"
        attribute "weeklyrainin", "string"
        attribute "winddir", "string"
        attribute "winddirection", "string"
        attribute "windgustmph", "string"
        attribute "windspeedmph", "string"
        attribute "ultravioletIndexDisplay", "string"
        // End of Ambient Weather API Rest MAP

        // Weather Forecast & Misc attributes
        attribute "moonAge", "number"
        attribute "rainForecast", "string"
        attribute "windPhrase", "string"
        attribute "lastSTupdate", "string"
        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "weatherIcon", "string"
        attribute "secondaryControl", "string"
        attribute "forecastIcon", "string"
        attribute "scheduleFreqMin", "string"
        attribute "sunriseDate", "string"
        attribute "sunsetDate", "string"
        attribute "alertDescription", "string"
        attribute "alertMessage", "string"
        attribute "version", "string"
        attribute "date", "string"

        command "refresh"
        
## Hubitat Installation

1. Install/paste the raw code from the links below into the respective Hubitat **'Apps Code'** and **'Drivers Code'** views.
2. Select Ambient Weather Station from the **'+Add User App'** of the Hubitat **'Apps View'**
3. Create a new Dashboard named 'Ambient Weather Station' and add the Ambient Weather Station devices that were created with the name of your Ambient Weather Station and Console.
4. Add additional data tiles to your Ambient Weather Station dashboard using the 'attribute' template of your selected Ambient Weather Station.
5. Create service accounts on the following Hubitat™ supported notification platforms & respective child devices per instructions for:
   * [Twillo](https://docs.hubitat.com/index.php?title=Twilio)
   * [Pushover](https://docs.hubitat.com/index.php?title=Pushover) 
7. If you want to start with the standard tile layout below, copy the layout.json from the link below and past the JSON String into the advanced section labeled layout of the gear icon (Settings) in your Ambient Weather Station dashboard.  

| Type |  Name   | Link |
|------------|:-------------------:|-------------------|
| Apps | Ambient Weather Station | [Raw Apps Code Link](https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/apps/ambient-weather-station.app) |
| Drivers | Ambient Weather Station | [Raw Drivers Code Link](https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/drivers/ambient-weather-station.driver) |
| Drivers | Ambient Weather Station Remote Sensor | [Raw Drivers Code Link](https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/drivers/ambient-weather-station%20remote-sensor.driver) |
| Drivers | SmartWeather Station Tile | [Raw Drivers Code Link](https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/drivers/smartweather-station-tile.driver) |
| Drivers | Ambient Particulate Monitor | [Raw Drivers Code Link](https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/drivers/ambient-particulate-monitor.driver) |
| JSON  | Example Layout | [Raw Layout Code Link](https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/layout/layout.json) |

<br>
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/hubitat-dashboard.jpg">
</p>

## Known Issues
### SmartThings™

1. As of release 4.10, the 'Units of Measure' for Temperature, Wind, Rain and Barometric values shown on the DTH are set from the Ambient Weather Station SmartApp and NOT your [Ambient Dashboard Unit Settings](https://dashboard.ambientweather.net/settings).  If you have not re-run the Ambient Weather Station SmartApp setup when migrating to release 4.10 or higher, the units of measure will be set from your SmartThings hub's location Temperature Setting as either all imperial (F) or all metric (C).
2. Setting this application's Refresh rate to 1 minute may cause an occasional ST console debug log "excessive http requests" debug error from ST.  ST rate limits their external http calls to avoid blacklisting.  The application will re-send the Ambient Weather® API http request when it encounters a ST rate limiting error.
3. SmartThings devices force a 'round down' on ALL displayed numeric values in the devices' Tile less than 0.1 to GT 0. Therefore, when an Ambient sensor reports a sensor that is below 0.1 and GT 0, this app will round the numeric value up to .1.  To get at the unrounded values, please use the attribute names with a suffix of '_real'.
4. Only enter +-NNNNN.NNNN,+-NNNNN.NNNN for latitude and longitude coordinates.  Do not enter a degree symbol or spaces for latitude, longitude coordinates in the Zipcode field.
5. 2. During the initialization of the Ambient Weather Station App, you may encounter a "groovyx.net.http.HttpResponseException: Too Many Requests" condition in the log, please just press done and re-try.

### Hubitat™
1. Local Weather data is not provided by Ambient Weather Station App in this version for Hubitat™.  Please install the built-in Hubitat Openweather™ device to generate local weather data.
2. During the initialization of the Ambient Weather Station App, you may encounter a "groovyx.net.http.HttpResponseException: Too Many Requests" condition in the log, please just refresh the page and it will attempt to connect to Ambient's web servers again.


## Previous Old/Legacy Versions
*(Available in Releases and 'Depreciated Versions' GitHub Branch)*

	- V3 Depreciated 3/5/2019 
	- V2 Depreciated 12/01/2018 
	- V1 Depreciated 06/01/2018





