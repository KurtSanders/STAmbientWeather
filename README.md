# STAmbientWeather V3 (2019)
*SmartThings Integration for Ambient Weather Stations*

### Description:

A custom SmartThings SmartApp Service Manager and Device Handler (DTH) which provides a connection to the weather data generated from your personal [Ambient weather station](https://www.ambientweather.com/ambientnet.html).  

This SmartThings application (V3) provides access to your [Ambientweather.net](https://ambientweather.net/) weather data via the [AmbientWeather API](https://ambientweather.docs.apiary.io/#).  The user can set the SmartThings Tile update/refresh rate of the weather data from either a manual or automatic refresh cycle (1 min to 180 mins (3 hours)).

[Read Version Release Features](https://github.com/KurtSanders/STAmbientWeather/wiki/Features-by-Version)

### Weather Station Tile and Details View
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V3-MobileClient1.PNG" width="300">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V3-MobileClient2.PNG" width="300">
</p>

### Remote Sensor Tile and Details View
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V3-MobileClient3.PNG" width="300">
</p>

## Requirements:
1. A personal [Ambient Weather Station](https://www.ambientweather.com/ambientnet.html) which connects to the Ambient Weather Network: (e.g., Model 2902A for example) and optionally  up to 8 Ambient remote temperature/hydro sensor(s).
2. SmartThings Hub
3. Supported mobile device with ST Legacy Client.  Android based ST Clients are encouraged to select the NO COLOR option during install to view value tiles without color. 
4. A working knowledge of the SmartThings IDE
	* Installing a SmartApp & DTH from a GitHub repository (see [SmartThings GitHub IDE integration documentation](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github) for example instructions and use the Repository Owner, Name and Branch from installation instructions below)
5. Ambient Weather Station API Key (Required)
	* An Ambient Weather Station API Key CAN BE SELF GENERATED FROM YOUR [AMBIENT DASHBOARD ACCOUNT VIEW](https://dashboard.ambientweather.net/account) and is used to securely connect this application to your Ambient weather station data. 

	Here is where to enter your API key in the ST IDE Editor for the SmartApp

	<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-APIKey2.jpg" width="300">
</p>

		 
## Installation & Configuration

**GitHub Repository Integration**

Create a new SmartThings Repository entry in your SmartThings IDE under 'Settings' with the following values:

| Name | Value |
|------|-------|
|Owner | **kurtsanders** |
|Name: | **STAmbientWeather**|
|Branch| **master**|

**Required Files in your SmartThings IDE Repository**

| IDE Repository    | Filename |
|-------------------|----------|
| My SmartApps      | kurtsanders : Ambient Weather Station Service Manager V3   |
| My Device Handler | kurtsanders : Ambient Weather Station V3<br>kurtsanders : Ambient Weather Station V3 No Color Tiles<br>kurtsanders : Ambient Weather Station Remote Sensor V3|

**Instructions**

1. Using the 'Update from REPO' button in the SmartThings IDE, Check the Ambient Weather Station Service Manager V3 SmartApp and publish & save.  Do the same from the "My Device Handlers" for the three (3) Ambient Weather device handlers ([See GitHub IDE integration](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github)) from this STAmbientWeather(master) repository to your SmartThings IDE.
2. **Required Next Step before setup on mobile client:** You must edit the newly added Ambient Weather Station Reporter V3 SmartApp in the IDE SmartApps browser Tab 'App Settings' and enter your apiString (Your Ambient API Key).  Update/Save your changes.
3. Locate the Ambient Weather Station Reporter V3 app in the MarketPlace/SmartApps/My Apps list and click to launch the smartapp.
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-Ambient Weather Station.PNG" width="300">
</p>

4. Update the following fields
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-SmartApp-Page2.PNG" width="300">
</p>

* Preferences
	- zipCode	(Your Zipcode for Weather Forecast Info)
	- Run Weather Station Refresh ('0' is 'Off/Manual Refresh')
	- 	(# mins for the APP to update weather values from your weather station)
	- Remove Color Background in Tiles for Displaying Values *(Only functions during initial installation/setup)*
	- Select Solar Radiation Units of Measure
	- IDE Logging (Optional)
		- debugVerbose	
		- infoVerbose	bool	
		-  Weather Underground API Calls
5. If you have Ambient remote temperature/hydro sensors attached to your ambient weather network station, you will be presented with a "remote sensor" screen in which you **must provide** a short descriptive name for each using only alpha or numeric characters.  The remote sensors will be numbered on the screen according to their dip switch setting.
6. Display the new SmartThings Tile in your ST Mobile Client

## ActionTiles™ and STAmbientWeather V3
* Only a subset of Ambient weather attributes can be viewed from [ActionTiles™](https://www.actiontiles.com/). Per ActionTiles™ website, ActionTiles™ **only** [supports Things](https://support.actiontiles.com/knowledge-bases/8/articles/3556-compatible-smartthings-capabilities-device-type-abstractions) that are fully compliant with a standard Capability (device type abstraction) as defined by SmartThings Capabilities. 
* SmartThings publishes a [partial list](https://www.smartthings.com/products) of "WWST" (Works with SmartThings) Certified Devices but not all of those are compatible with ActionTiles, because some of them claim a "Capability" that ActionTiles™ has not built a Tile Type for.
* Units of measure on ActionTiles™ cannot be modified

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTilesColorTiles.jpg" width="600">
</p>

| Name of Action Tile Thing (Checkmark) | Ambient Weather Attribute |
|------------|-------------------|
| Energy<p>Power | Wind Speed, mph |
| Illuminance Measurement | Solar Radiation, lux or w/m²<br>Light, lux or w/m² |
| Motion Sensor | Wind Speed > 0 Detected, active |
| Relative Humidity Measurement | Outside Relative Humidity, % |
| Temperature Measurement | Outside Temperature, °F |
| Ultraviolet Index | Ultraviolet Index, uv |
| Water Sensor | Rain Detected, wet/dry |

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTilesV3Things.jpg" width="600">
</p>

## Known Issues
1. Units of measure shown on the DTH are set from your [Ambient Dashboard Unit Settings](https://dashboard.ambientweather.net/settings) and this ST application has only been tested using USA imperial units setting.
2. Setting this application's Refresh rate to 1 minute may cause an occasional ST console debug log "excessive http requests" debug error from ST.  ST rate limits their external http calls to avoid blacklisting.  The application will re-send the Ambient Weather API http request when it encounters a ST rate limiting error.
3. The V3 version is the ONLY supported release in 2019.  Please upgrade previous versions if you desire new features, bug fixes, etc. 
4. Android o/s mobile devices render data values much better with the **NO COLOR** preference option selected ON during installation.  If one desires to change to a COLOR or NO COLOR background tile mode, they must do so in the ST IDE in My Devices by changing the DTH name for that device accordingly (See Table above for filenames of DTH's for this V3 release).  One can always remove and re-install the application and select the correct NO COLOR Option as well.

## Previous Old Versions
*(Available in 'Depreciated Versions' GitHub Branch)*

	- V2 Depreciated 12/01/2018 
	- V1 Depreciated 06/01/2018

## Capabilities, Attributes & Commands
The following device capabilities, attributes and commands are available for your own subscriptions from WebCore(tm) or a custom smartApp.

        // Start of Ambient Weather capability 
        capability "Illuminance Measurement"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Refresh"
        capability "Water Sensor"
        capability "Ultraviolet Index"
    
        // Wind Motion Detection
        capability "Motion Sensor"
        // Wind Speed Pseudo Capability
        capability "Power Meter"
        capability "Energy Meter"


        // Start of Ambient Weather API Rest MAP
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

        command "refresh"
        





