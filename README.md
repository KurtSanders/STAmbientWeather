# STAmbientWeather V2
*SmartThings Integration for Ambient Weather Stations*

## Description:

A custom SmartThings SmartApp Service Manager and Device Handler (DTH) which provides a connection to all the data generated from your personal [Ambient weather station](https://www.ambientweather.com/ambientnet.html).  This SmartThings application (V2) provides access to your [Ambientweather.net](https://ambientweather.net/) weather data via the [AmbientWeather API](https://ambientweather.docs.apiary.io/#).  The user can set the SmartThings Tile update/refresh rate of the weather data from either manual or automatic (1 min to 180 mins (3 hours)).
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/V2-Tile.PNG" width="300">
</p>

## Requirements:
1. A personal [Ambient Weather Station](https://www.ambientweather.com/ambientnet.html) which connects to the Ambient Weather Network: (e.g. Model 2902A for example)
2. SmartThings Hub
3. Supported mobile device with ST Legacy Client
4. A working knowledge of the SmartThings IDE
	* Installing a SmartApp & DTH from a GitHub repository (see [SmartThings GitHub IDE integration documentation](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github) for example instructions and use the Repository Owner, Name and Branch from installation instructions below)
5. Ambient Weather Station API/APP Keys (Required)
	* API Key String
	* Application Key String
 
	An Ambient Weather Station API Key is used to securely connect this program to your Ambient weather station data. 
	
	You must request an Ambient Weather Station Application Key.
	 
	* Email [support@ambientweather.com](mailto:support@ambientweather.com) and include a brief description of need, ie. "connecting to my weaather station from a local application". You must also include the MAC address of your weather station in the email.

## Installation & Configuration of the SmartThings DTH

**GitHub Repository Integration**

Create a new SmartThings Repository in the SmartThings IDE under 'Settings' with the following:

| Name | Value |
|------|-------|
|Owner | **kurtsanders** |
|Name: | **STAmbientWeather**|
|Branch| **beta**|


1. Add a new SmartApp & device handler ([See GitHub IDE integration](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github)) from  the STAmbientWeather(beta) respository.
2. **Required Next Step:** You must edit the newly added Ambient Weather Station Reporter V2 SmartApp in the IDE SmartApps browser Tab 'App Settings' and enter your apiString (Your Ambient API Key) and appString (Your Ambient App Key).  Update/Save your changes.
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/V2-SmartApp-Page1.PNG" width="300">
</p>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/V2-APIKey1.jpg" width="400">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/V2-APIKey2.jpg" width="300">
</p>
3. Locate the Ambient Weather Station Reporter V2 app in the MarketPlace/SmartApps/My Apps list and click to launch the smartapp.
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/V2-Ambient Weather Station.PNG" width="300">
</p>

4. Update the following fields
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/V2-SmartApp-Page2.PNG" width="300">
</p>

	* Preferences
		* zipCode	(Your Zipcode for Weather Forecast Info)
		* schedulerFreq	(# mins for the APP to update weather values from your weather station)
		* IDE Logging (Optional)
			* debugVerbose	
			* infoVerbose	bool	
			* Weather Underground API Calls
5. Display the new SmartThings Tile in your ST Mobile Client

## Capabilities, Attributes & Commands
The following device capabilities, attributes and commands are available for your own subscriptions from WebCore(tm) or an custom smartApp.

        // Start of Ambient Weather capability 
        capability "Illuminance Measurement"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Refresh"
        capability "Motion Sensor"
        capability "Water Sensor"
        capability "Ultraviolet Index"

        // Start of Ambient Weather Attributes
        attribute "baromabsin", "string"
        attribute "baromrelin", "string"
        attribute "city", "string"
        attribute "dailyrainin", "string"
        attribute "date", "string"
        attribute "dateutc", "string"
        attribute "dewPoint", "string"
        attribute "eventrainin", "string"
        attribute "feelsLike", "string"
        attribute "hourlyrainin", "string"
        attribute "humidity", "string"
        attribute "humidityin", "string"
        attribute "lastRain", "string"
        attribute "location", "string"
        attribute "lastRainDuration", "string"
        attribute "macAddress", "string"
        attribute "maxdailygust", "string"
        attribute "monthlyrainin", "string"
        attribute "name", "string"
        attribute "temperature", "string"
        attribute "tempinf", "string"
        attribute "totalrainin", "string"
        attribute "weeklyrainin", "string"
        attribute "winddir", "string"
        attribute "winddirection", "string"
        attribute "windgustmph", "string"
        attribute "windspeedmph", "string"   
        // End of Ambient Weather API Rest MAP
        attribute "moonAge", "number"
        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "weatherIcon", "string"
        attribute "forecastIcon", "string"
        attribute "scheduleFreqMin", "string"
        attribute "sunriseDate", "string"
        attribute "sunsetDate", "string"
        attribute "alertDescription", "string"
        attribute "alertMessage", "string"
        attribute "version", "string"
        
        // Start of Ambient Weather Device Commands
        command "refresh"

## Known Issues
1. Units of measure are set from your [Ambient Dashboard Unit Settings](https://dashboard.ambientweather.net/settings) and this ST application has only been tested using USA Imperials settings.
2. Setting this application's Refresh rate to 1 minute may cause an occaisonal "excesssive http requests" debug error from ST.  ST rate limits their external calls to avoid blacklisting.
3. This V2 application was re-developed to be a cooperative Service Manager SmartApp & Device Tile for compatibility with the Android ST clients.  If one prefers this apllication writen for V1 (device only tile with preferences in the device gear), V1 will only display on iOS devices. 



