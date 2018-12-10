# STAmbientWeather V2
*SmartThings Integration for Ambient Weather Stations*

## Description:

A custom SmartThings SmartApp Service Manager and Device Handler (DTH) which provides a connection to the weather data generated from your personal [Ambient weather station](https://www.ambientweather.com/ambientnet.html).  This SmartThings application (V2) provides access to your [Ambientweather.net](https://ambientweather.net/) weather data via the [AmbientWeather API](https://ambientweather.docs.apiary.io/#).  The user can set the SmartThings Tile update/refresh rate of the weather data from either manual or automatic (1 min to 180 mins (3 hours)).
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-Tile.PNG" width="300">
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
	 
	* Email [support@ambientweather.com](mailto:support@ambientweather.com) and include a brief description of need, ie. "connecting to my weather station from a local application". You must also include the MAC address of your weather station in the email.

## Installation & Configuration of the SmartThings DTH

**GitHub Repository Integration**

Create a new SmartThings Repository in the SmartThings IDE under 'Settings' with the following:

| Name | Value |
|------|-------|
|Owner | **kurtsanders** |
|Name: | **STAmbientWeather**|
|Branch| **master**|

1. Add a new SmartApp & device handler ([See GitHub IDE integration](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github)) from  the STAmbientWeather(master) repository.
2. **Required Next Step:** You must edit the newly added Ambient Weather Station Reporter V2 SmartApp in the IDE SmartApps browser Tab 'App Settings' and enter your apiString (Your Ambient API Key) and appString (Your Ambient App Key).  Update/Save your changes.
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-SmartApp-Page1.PNG" width="300">
</p>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-APIKey1.jpg" width="400">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-APIKey2.jpg" width="300">
</p>
3. Locate the Ambient Weather Station Reporter V2 app in the MarketPlace/SmartApps/My Apps list and click to launch the smartapp.
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

## Using STAmbientWeather V2 with [ActionTiles](https://www.actiontiles.com/) Application

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTile-1.jpg">
</p>

*  Configure your Ambient Weather Station to publish it's local weather data to [Weather Underground](https://www.wunderground.com) as in the example [“KOHXENIA30”](https://www.wunderground.com/personal-weather-station/dashboard?ID=KOHXENIA30#history/s20180829/e20180905/mweek).  This configuration can be completed on the Weather Station console during setup or on the [Ambient Dashboard My Devices](https://dashboard.ambientweather.net/devices)
* You will need to have or create a Weather Underground account and connect this account to your Ambient WS account.  You will need your WU station ID WU account name and password.
* Manually add a second ST device in the ST IDE's 'My Devices' and specify 'SmartWeather Station Tile' as the DTH.  Once the new device is setup, you can view the device in your ST mobile client and use the zipcode preference of your Weather Underground station ID (ie. pws:KOHXENIA30) for which the SmartWeather Station Tile will pull your Ambient Weather.

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTile-2.jpg" >
</p>

* Create a second AT tile from the SmartWeather Station Tile” from the PWS zipcode ST device.

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTile-3.jpg" width=300">
</p>

* Add your new weather device to ActionTiles 

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTile-4.jpg" width=300">
</p>

## Known Issues
1. Units of measure shown on the DTH are set from your [Ambient Dashboard Unit Settings](https://dashboard.ambientweather.net/settings) and this ST application has only been tested using USA imperial units setting.
2. Setting this application's Refresh rate to 1 minute may cause an occasional ST console debug log "excessive http requests" debug error from ST.  ST rate limits their external http calls to avoid blacklisting.  The application will re-send the Ambient Weather API http request when it encounters a ST rate limiting error.
3. This V2 application was re-developed to be a cooperative SmartThings Service Manager SmartApp & Device Tile.  If one prefers a DTH Only, this Ambient Weather Station application written for V1 (device handler (DTH) only tile with preferences set in the device gear), V1 is available in the Github repository and can be installed to the IDE's 'My Device Handlers'.  One must manually create a new device in the IDE's 'My Devices' and refer to this DTH.




