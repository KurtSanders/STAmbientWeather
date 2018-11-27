# STAmbientWeather V2
*SmartThings Integration for Ambient Weather Stations*

## Description:

A custom SmartThings SmartApp Service Manager and Device Handler (DTH) which connects to the data generated from an [Ambient weather station](https://www.ambientweather.com/ambientnet.html).  This SmartThings device handler accesses the [Ambientweather.net](https://ambientweather.net/) weather data using the AmbientWeather API which is near realtime.  The user can set the refresh rate of the weather data from 1 min to 180 mins (3 hours).

![STAmbientWeather logo](https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/STMobileClient.PNG)

## Requirements:
1. [Ambient Weather Station](https://www.ambientweather.com/ambientnet.html) which connects to the Ambient Weather Network: (e.g. Model 2902A for example)
2. SmartThings Hub
3. Supported mobile device with ST Client:
	* Apple iOS device or 
	* Android mobile device.  *Known issue: The Android O/S ST client may not be able to display the custom weather tile, but Ambient weather API attributes are accessible from ST WebCore, etc*
4. A working knowledge of the SmartThings IDE
	* Installing a SmartApp & DTH from a GitHub repository (see [WebCore Wiki](https://wiki.webcore.co/) for example instructions and use the repository Owner, Name and Branch from below)
5. Ambient Weather Station API/APP Keys (Required)
	* API Key
	* Application Key
 
	An Ambient Weather Station API Key is used to securely connect this program to your Ambient weather station data. 
	
	You must request an Ambient Weather Station Application Key.
	 
	* Email [support@ambientweather.com](mailto:support@ambientweather.com) and include a brief description of need, ie. "connecting to my weaather station from a local application". You must also include the MAC address of your weather station in the email.

## Installation & Configuration of the SmartThings DTH

**GitHub Repository Integration**

Create a new SmartThings Repository in the SmartThings IDE under 'Settings':

* Owner: 		KurtSanders
* Name:		STAmbientWeather
* Branch: 	Beta

1. Create a new SmartApp & device handler by installing the 'STAbientWeather' SmartApp & DTH into the SmartThings IDE "SmartApps" and "My Device Handlers".  Make sure to 'Save' and 'Publish' the new SmartApp and DTH
2. **Required:** Edit the Ambient Weather Station Reporter SmartApp in the IDE SmartApps browser Tab 'App Settings' and enter your apiString (Your Ambient API Key) and appString (Your Ambient App Key).  Update/Save
2. Locate the Ambient Weather Station Reporter in the MarketPlace/SmartApps/My Apps and click to launch the smartapp:
	* Preferences
		* zipCode	(Your Zipcode for Weather Forecast Info)
		* schedulerFreq	(# mins for the APP to update weather values from your weather station)
		* IDE Logging
			* debugVerbose	
			* infoVerbose	bool	
			* Weather Underground API Calls
3. Display the new SmartThings Tile in your ST Mobile Client



