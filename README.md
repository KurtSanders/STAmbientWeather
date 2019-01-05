# STAmbientWeather V3 (2019)
*SmartThings Integration for Ambient Weather Stations*

## Previous Versions
* V2 Depreciated 12/01/2018
* V1 Depreciated 06/01/2018

## Description:

A custom SmartThings SmartApp Service Manager and Device Handler (DTH) which provides a connection to the weather data generated from your personal [Ambient weather station](https://www.ambientweather.com/ambientnet.html).  This SmartThings application (V3) provides access to your [Ambientweather.net](https://ambientweather.net/) weather data via the [AmbientWeather API](https://ambientweather.docs.apiary.io/#).  The user can set the SmartThings Tile update/refresh rate of the weather data from either manual or automatic (1 min to 180 mins (3 hours)).

## Weather Station Tile and Details
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V3-MobileClient1.PNG" width="300">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V3-MobileClient2.PNG" width="300">
</p>

## Remote Sensor Detail Tile
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V3-MobileClient3.PNG" width="300">
</p>

## Requirements:
1. A personal [Ambient Weather Station](https://www.ambientweather.com/ambientnet.html) which connects to the Ambient Weather Network: (e.g. Model 2902A for example) and optionally  up to 8 Ambient remote temperature/hydro sensor(s).
2. SmartThings Hub
3. Supported mobile device with ST Legacy Client.  Installation **MUST** be completed on an Apple iOS device as the Android ST Client causes severe errors.  Android ST Client can view installed tiles after the Apple ST Client install. 
4. A working knowledge of the SmartThings IDE
	* Installing a SmartApp & DTH from a GitHub repository (see [SmartThings GitHub IDE integration documentation](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github) for example instructions and use the Repository Owner, Name and Branch from installation instructions below)
5. Ambient Weather Station API Key (Required)
	* An Ambient Weather Station API Key CAN BE SELF GENERATED FROM YOUR [AMBIENT DASHBOARD ACCOUNT VIEW](https://dashboard.ambientweather.net/account) and is used to securely connect this application to your Ambient weather station data. 
		 
## Installation & Configuration of the SmartThings DTH

**GitHub Repository Integration**

Create a new SmartThings Repository in the SmartThings IDE under 'Settings' with the following:

| Name | Value |
|------|-------|
|Owner | **KurtSanders** |
|Name: | **STAmbientWeather**|
|Branch| **master**|

1. Add a new SmartApp & device handler ([See GitHub IDE integration](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github)) from  the STAmbientWeather(master) repository.
2. **Required Next Step:** You must edit the newly added Ambient Weather Station Reporter V3 SmartApp in the IDE SmartApps browser Tab 'App Settings' and enter your apiString (Your Ambient API Key).  Update/Save your changes.
3. Locate the Ambient Weather Station Reporter V3 app in the MarketPlace/SmartApps/My Apps list and click to launch the smartapp.
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-Ambient Weather Station.PNG" width="300">
</p>

4. Update the following fields
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-SmartApp-Page2.PNG" width="300">
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
The following device capabilities, attributes and commands are available for your own subscriptions from WebCore(tm) or a custom smartApp.

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
        attribute "lastSTupdate", "string"
        
        // Start of Ambient Weather Device Commands
        command "refresh"


## Known Issues
1. Units of measure shown on the DTH are set from your [Ambient Dashboard Unit Settings](https://dashboard.ambientweather.net/settings) and this ST application has only been tested using USA imperial units setting.
2. Setting this application's Refresh rate to 1 minute may cause an occasional ST console debug log "excessive http requests" debug error from ST.  ST rate limits their external http calls to avoid blacklisting.  The application will re-send the Ambient Weather API http request when it encounters a ST rate limiting error.
3. The V3 version is the ONLY supported release in 2019.  Please upgrade previous versions if you desire new features, bug fixes, etc. 
4. The V2 application was developed to be a cooperative SmartThings Service Manager SmartApp & Device Tile.  If one prefers a DTH Only, this Ambient Weather Station application written for V1 (device handler (DTH) only tile with preferences set in the device gear), V1 is available in the Github repository and can be installed to the IDE's 'My Device Handlers'.  One must manually create a new device in the IDE's 'My Devices' and refer to this DTH.




