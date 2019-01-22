# STAmbientWeather V3 (βeta)
*SmartThings Integration for Ambient Weather Stations*

### Description:

A custom SmartThings SmartApp Service Manager and Device Handler (DTH) which provides a connection to the weather data generated from your personal [Ambient weather station](https://www.ambientweather.com/ambientnet.html).  

This SmartThings application (V3) provides access to your [Ambientweather.net](https://ambientweather.net/) weather data via the [AmbientWeather API](https://ambientweather.docs.apiary.io/#).  The user can set the SmartThings Tile update/refresh rate of the weather data from either a manual or automatic refresh cycle (1 min to 180 mins (3 hours)).

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/readme.png" width="50">[Changelog & Version Release Features](https://github.com/KurtSanders/STAmbientWeather/wiki/Features-by-Version)

## Weather Station Tile and Details View
### Screenshots of Device Attributes

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V303-MobileClient1.PNG" width=200>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V303-MobileClient2.PNG" width=200>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V303-MobileClient3.PNG" width=200>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V303-MobileClient4.PNG" width=200></p>

### Ambient Weather Station with multiple remote sensors

<p align="center"><img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V3-MobileClient1.PNG" width=200>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/V303-MobileClient5.PNG" width=200>
</p>

### Weather Event SMS Alerts

Select the following events to send Weather SMS Text Alerts for user defined values:

   * Low Temperature (If Outside Temp is reported by Weather Station)
   * High Temperature (If Outside Temp is reported by Weather Station)
   * Rain Detected (If Rain is reported by Weather Station)
   * Severe Weather (For Zipcode) 

Weather SMS Alerts are sent per user preferences for once every {1,2,4,6,12,24} hours. 

<p align="center"><img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/screenshots/Weather Event Alerts.PNG" width=300>
</p>


### Requirements:

<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/readme.png" width="50"> **YOU DO NOT NEED AN AMBIENT DEVELOPERS APP KEY**

* Your Ambient Weather Station **API Key** (Required)
	* An Ambient Weather Station **API Key** CAN BE SELF GENERATED FROM YOUR [AMBIENT DASHBOARD ACCOUNT VIEW](https://dashboard.ambientweather.net/account). The API key is used to securely connect this SmartThings application to your personal Ambient weather station data. Just locate your Ambient API key from your Ambient weather station account.

### Enter your Ambient API key in the ST IDE Editor for the SmartApp in the Settings section.

<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-APIKey1.jpg" width="500"><br><br>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/V2-APIKey2.jpg" width="400">
</p>

2. A personal [Ambient Weather Station](https://www.ambientweather.com/ambientnet.html) which connects to the Ambient Weather Network: (e.g., Model 2902A for example) and optionally  up to 8 Ambient remote temperature/hydro sensor(s).
3. SmartThings Hub
4. Supported mobile device with ST Legacy Client.  Android based ST Clients are encouraged to select the NO COLOR option during install to view value tiles without color. 
5. A working knowledge of the SmartThings IDE
	* Installing a SmartApp & DTH from a GitHub repository (see [SmartThings GitHub IDE integration documentation](https://docs.smartthings.com/en/latest/tools-and-ide/github-integration.html?highlight=github) for example instructions and use the Repository Owner, Name and Branch from installation instructions below)

		 
## Installation & Configuration

**GitHub Repository Integration**

Create a new SmartThings Repository entry in your SmartThings IDE under 'Settings' with the following values:

| Name | Value |
|------|-------|
|Owner | **kurtsanders** |
|Name: | **STAmbientWeather**|
|Branch| **beta**|

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

### Ambient Weather Station ActionTiles™ Integration
<p align="center">
ActionTiles™
</p>
<p align="center">
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTilesWeatherTile.jpg" width="250"><br>
<img src="https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/ActionTilesWeatherTile2.jpg" width="550">

<br>Ambient Weather Station ActionTiles™ Integration</p>

1. A STAmbientWeather APP preference option is available to "Create a SmartWeather Station Tile for use as an Ambient Weather ActionTiles™ Weather Tile?".  
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
5. STAmbientWeather V3 recognizes ONE Ambient weather station and will add the first station's data and ignore the others.
6. Multiple instances of STAmbientWeather app are not currently supported.

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
        





